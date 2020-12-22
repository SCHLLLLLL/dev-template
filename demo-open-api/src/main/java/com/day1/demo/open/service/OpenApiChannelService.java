package com.day1.demo.open.service;

import com.day1.demo.common.constant.RedisKeyConstant;
import com.day1.demo.common.utils.CacheUtilService;
import com.day1.demo.mapper.model.DsOpenApiChannel;
import com.day1.demo.mapper.model.DsOpenApiChannelInterface;
import com.day1.demo.mapper.service.DsOpenApiChannelInterfaceService;
import com.day1.demo.mapper.service.DsOpenApiChannelService;
import com.day1.demo.open.service.bo.ApiChannelBo;
import com.day1.demo.open.utils.LocalCacheUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 14:35
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OpenApiChannelService {

    private final DsOpenApiChannelService dsOpenApiChannelService;
    private final DsOpenApiChannelInterfaceService dsOpenApiChannelInterfaceService;

    public ApiChannelBo getApiChannel(String appId) {

        String apiBoCacheKey = RedisKeyConstant.getOpenApiChannelApiBoCacheKey(appId);
        //本地缓存 一级缓存
        ApiChannelBo apiChannelBo = (ApiChannelBo) LocalCacheUtils.get(apiBoCacheKey);

        if (Objects.nonNull(apiChannelBo)) {
            return apiChannelBo;
        }

        //redis 二级缓存
        apiChannelBo = CacheUtilService.get(apiBoCacheKey, ApiChannelBo.class);

        if (Objects.nonNull(apiChannelBo)) {
            return apiChannelBo;
        }

        DsOpenApiChannel dsOpenApiChannel = dsOpenApiChannelService.
                getOne(dsOpenApiChannelService.getWrapper().eq(DsOpenApiChannel::getAppId, appId));

        if (Objects.isNull(dsOpenApiChannel)) {
            apiChannelBo = new ApiChannelBo(new DsOpenApiChannel(), Collections.emptyList());
            CacheUtilService.setJson(apiBoCacheKey, apiChannelBo, 10, TimeUnit.MINUTES);
            LocalCacheUtils.put(apiBoCacheKey, apiChannelBo);
            return apiChannelBo;
        }

        List<DsOpenApiChannelInterface> channelInterfaceList =
                dsOpenApiChannelInterfaceService.list(dsOpenApiChannelInterfaceService.getWrapper().
                        eq(DsOpenApiChannelInterface::getChannelId, dsOpenApiChannel.getChannelId()));

        if (CollectionUtils.isEmpty(channelInterfaceList)) {
            apiChannelBo = new ApiChannelBo(new DsOpenApiChannel(), Collections.emptyList());
            CacheUtilService.setJson(apiBoCacheKey, apiChannelBo, 10, TimeUnit.MINUTES);
            LocalCacheUtils.put(apiBoCacheKey, apiChannelBo);
            return apiChannelBo;
        }

        apiChannelBo = new ApiChannelBo(dsOpenApiChannel, channelInterfaceList);
        CacheUtilService.setJson(apiBoCacheKey, apiChannelBo, 10, TimeUnit.MINUTES);
        LocalCacheUtils.put(apiBoCacheKey, apiChannelBo);
        return apiChannelBo;
    }
}
