package com.day1.demo.open.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.day1.demo.common.constant.RedisKeyConstant;
import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.logback.log.DemoLogger;
import com.day1.demo.common.utils.CacheUtilService;
import com.day1.demo.common.utils.DateUtil;
import com.day1.demo.mapper.enums.DsOpenApiUriEnum;
import com.day1.demo.mapper.enums.EnableStatus;
import com.day1.demo.mapper.enums.TimeUnitEnum;
import com.day1.demo.mapper.model.DsOpenApiChannel;
import com.day1.demo.mapper.model.DsOpenApiChannelInterface;
import com.day1.demo.open.config.filter.wrapper.HttpRequestWrapper;
import com.day1.demo.open.holder.AppIdHolder;
import com.day1.demo.open.service.OpenApiChannelService;
import com.day1.demo.open.service.bo.ApiChannelBo;
import com.day1.demo.open.utils.LocalCacheUtils;
import com.day1.demo.open.utils.SignUtils;
import com.google.common.annotations.VisibleForTesting;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * @author: LinHangHui
 * @Date: 2020/12/9 21:15
 */
@Component
public class SignInterceptor extends HandlerInterceptorAdapter {

    private final DemoLogger logger = new DemoLogger(SignInterceptor.class);

    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";

    private static final String APP_ID = "appId";
    public static final String SIGN = "sign";
    private static final String TIMESTAMP = "timestamp";

    @Value("${ds.open.api.invincibleSign:linhanghuidashuaibi}")
    private String invincibleSign;
    /**
     * invincibleSign签名有效期,单位:秒
     */
    @Value("${ds.open.api.sign-timestamp-available-period-second:30}")
    private Integer signTimestampAvailablePeriodSecond;

    @Setter(onMethod = @__(@Autowired))
    private OpenApiChannelService openApiChannelService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 如果不是映射到方法直接通过
         */
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Map<String, Object> params = getBody(request);

        String appId = Optional.ofNullable(params.get(APP_ID)).map(String::valueOf).orElse(null);
        String sign = Optional.ofNullable(params.get(SIGN)).map(String::valueOf).orElse(null);
        String timestamp = Optional.ofNullable(params.get(TIMESTAMP)).map(String::valueOf).orElse(null);

        if (StringUtils.isBlank(appId) || StringUtils.isBlank(sign) || StringUtils.isBlank(timestamp)) {

            throw new BaseException(ExceptionType.PERMISSION_DENIED);
        }

        if (!invincibleSign.equals(sign)) {
            if (Math.abs(System.currentTimeMillis() / 1000 - Long.parseLong(timestamp))
                    > signTimestampAvailablePeriodSecond) {
                throw new BaseException(ExceptionType.PERMISSION_DENIED);

            }
        }

        ApiChannelBo apiChannelBo = openApiChannelService.getApiChannel(appId);

        boolean signFlag = checkSignInvalid(params, apiChannelBo.getApiChannel().getAppKey(), sign);

        if (!StringUtils.equals(sign, invincibleSign) && !signFlag) {
            throw new BaseException(ExceptionType.PERMISSION_DENIED);
        }

        if (Objects.isNull(apiChannelBo.getApiChannel().getEnabled())
                || EnableStatus.DISABLE.equals(apiChannelBo.getApiChannel().getEnabled())) {
            throw new BaseException(ExceptionType.PERMISSION_DENIED);
        }

        DsOpenApiUriEnum uriEnum = DsOpenApiUriEnum.getByApiUri(request.getRequestURI());
        Optional<DsOpenApiChannelInterface> channelInterface = apiChannelBo.getChannelInterface(uriEnum);

        if (!channelInterface.isPresent()) {
            throw new BaseException(ExceptionType.PERMISSION_DENIED);
        }

        //TODO 限流


        AppIdHolder.set(appId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        AppIdHolder.remove();
    }


    private Map<String, Object> getBody(HttpServletRequest request) {

        Map<String, Object> data;

        HttpRequestWrapper wrapper = (HttpRequestWrapper) request;
        if (wrapper.getMethod().equals(METHOD_GET)) {
            data = convertParameterMap(request.getParameterMap());
        } else if (wrapper.getMethod().equals(METHOD_POST)) {
            data = JSON.parseObject(new String(wrapper.getBody(), StandardCharsets.UTF_8), new TypeReference<Map<String, Object>>() {});
        } else {
            throw new BaseException(ExceptionType.PERMISSION_DENIED, "不支持该类型调用");
        }
        return data;
    }

    private Map<String, Object> convertParameterMap(Map<String, String[]> parameterMap) {

        Map<String, Object> params = new TreeMap<>();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] valueArr = entry.getValue();
            if (valueArr == null) {
                params.put(entry.getKey(), null);
                continue;
            }
            if (valueArr.length > 1) {
                params.put(entry.getKey(), Arrays.asList(valueArr));
                continue;
            }

            params.put(entry.getKey(), valueArr[0]);
        }
        return params;
    }

    private boolean checkSignInvalid(Map<String, Object> params, String secret, String sign) {
        String md5Str = SignUtils.buildSign(params, secret);
        return StringUtils.equals(md5Str, sign);
    }


//    private boolean checkCallLimitIsOver(DsOpenApiChannel dsOpenApiChannel,
//                                         DsOpenApiChannelInterface dsOpenApiChannelInterface, String appId, DsOpenApiUriEnum dsOpenApiUriEnum) {
//
//        long endTimeForTotal =
//                Optional.ofNullable(LocalCacheUtils.get(RedisKeyConstant.getOpenApiQptBanForTotal(appId)))
//                        .map(String::valueOf).map(Long::valueOf).orElse(0L);
//
//        if (LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= endTimeForTotal) {
//            return true;
//        }
//
//        Long totalTimes = 0L;
//        if (dsOpenApiChannel.getTimeUnit() == TimeUnitEnum.DAY) {
//            //今天到明天间隔时间戳
//            totalTimes = CacheUtilService.increment(RedisKeyConstant.getOpenApiQptLimitForTotal(appId),
//                    DateUtil.getRemainMilliSecondsInDay());
//            //明天凌晨的时间戳
//            endTimeForTotal = DateUtil.getMilliSecondsInDay();
//
//        } else if (dsOpenApiChannel.getTimeUnit() == TimeUnitEnum.HOUR) {
//            totalTimes = CacheUtilService.increment(RedisKeyConstant.getOpenApiQptLimitForTotal(appId),
//                    DateUtil.getRemainMilliSecondsInHour());
//            endTimeForTotal = DateUtil.getMilliSecondsInHour();
//
//        } else if (dsOpenApiChannel.getTimeUnit() == TimeUnitEnum.MINUTE) {
//            totalTimes = CacheUtilService.increment(RedisKeyConstant.getOpenApiQptLimitForTotal(appId),
//                    DateUtil.getRemainMilliSecondsInMinute());
//            endTimeForTotal = DateUtil.getMilliSecondsInMinute();
//
//        } else if (dsOpenApiChannel.getTimeUnit() == TimeUnitEnum.SECOND) {
//            totalTimes = CacheUtilService.increment(RedisKeyConstant.getOpenApiQptLimitForTotal(appId),
//                    DateUtil.getRemainMilliSecondsInSeconds());
//            endTimeForTotal = DateUtil.getMilliSecondsInSeconds();
//
//        }
//
//        if (totalTimes > dsOpenApiChannel.getQptLimitTotal()) {
//            logger.warn("调用频率超过上限,appId:{}", appId);
//            if (endTimeForTotal != 0) {
//                LocalCacheUtils.put(RedisKeyConstant.getOpenApiQptBanForTotal(appId), endTimeForTotal);
//            }
//            return true;
//        }
//
//        long endTimeForInterface = Optional
//                .ofNullable(
//                        LocalCacheUtils.get(RedisKeyConstant.getOpenApiQptBanForInterface(appId, dsOpenApiUriEnum.getCode())))
//                .map(String::valueOf).map(Long::valueOf).orElse(0L);
//        if (LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= endTimeForInterface) {
//            return true;
//        }
//
//        Long interfaceTimes = 0L;
//        if (dsOpenApiChannelInterface.getTimeUnit() == TimeUnitEnum.DAY) {
//            interfaceTimes = CacheUtilService.increment(
//                    RedisKeyConstant.getOpenApiQptLimitForInterface(appId, dsOpenApiUriEnum.getCode()),
//                    DateUtil.getRemainMilliSecondsInDay());
//            endTimeForInterface = DateUtil.getMilliSecondsInDay();
//
//        } else if (dsOpenApiChannelInterface.getTimeUnit() == TimeUnitEnum.HOUR) {
//            interfaceTimes = CacheUtilService.increment(
//                    RedisKeyConstant.getOpenApiQptLimitForInterface(appId, dsOpenApiUriEnum.getCode()),
//                    DateUtil.getRemainMilliSecondsInHour());
//            endTimeForInterface = DateUtil.getMilliSecondsInHour();
//
//        } else if (dsOpenApiChannelInterface.getTimeUnit() == TimeUnitEnum.MINUTE) {
//            interfaceTimes = CacheUtilService.increment(
//                    RedisKeyConstant.getOpenApiQptLimitForInterface(appId, dsOpenApiUriEnum.getCode()),
//                    DateUtil.getRemainMilliSecondsInMinute());
//            endTimeForInterface = DateUtil.getMilliSecondsInMinute();
//
//        } else if (dsOpenApiChannelInterface.getTimeUnit() == TimeUnitEnum.SECOND) {
//            interfaceTimes = CacheUtilService.increment(
//                    RedisKeyConstant.getOpenApiQptLimitForInterface(appId, dsOpenApiUriEnum.getCode()),
//                    DateUtil.getRemainMilliSecondsInSeconds());
//            endTimeForInterface = DateUtil.getMilliSecondsInSeconds();
//
//        }
//
//        if (interfaceTimes > dsOpenApiChannelInterface.getQptLimitTotal()) {
//            logger.warn("调用频率超过上限,appId:{},interfaceIndex:{}", appId, dsOpenApiUriEnum.getApiUri());
//            if (endTimeForInterface != 0) {
//                LocalCacheUtils.put(RedisKeyConstant.getOpenApiQptBanForInterface(appId, dsOpenApiUriEnum.getCode()),
//                        endTimeForInterface);
//            }
//            return true;
//        }
//
//        return false;
//    }
}
