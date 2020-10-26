package com.day1.demo.inner.interceptor;

import com.day1.demo.common.anotation.IgnoreTokenAuthorization;
import com.day1.demo.common.auth.TokenService;
import com.day1.demo.common.auth.UserInfo;
import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.utils.ThreadLocalMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author: LinHangHui
 * @Date: 2020/10/26 15:08
 */
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    public final static String TOKEN_HEADER_NAME = "token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        String token = request.getHeader(TOKEN_HEADER_NAME);
        //Log.info("登录的token为{}",token)
        IgnoreTokenAuthorization tokenAuthorization = method.getAnnotation(IgnoreTokenAuthorization.class);
        if (null != tokenAuthorization && !tokenAuthorization.isEnabled()) {
            if (!StringUtils.isBlank(token)) {
                //校验token
                UserInfo userInfo = TokenService.getUserInfoByToken(token);
                //token验证成功，将token对应的用户存入ThreadLocal中
                ThreadLocalMap.pull(IgnoreTokenAuthorization.CURRENT_USER,userInfo);
            }
            return true;
        }
        if (StringUtils.isBlank(token)) {
            throw new BaseException(ExceptionType.TOKEN_INVLID_ERROR);
        }

        UserInfo userInfo = TokenService.getUserInfoByToken(token);
        if ((Objects.nonNull(userInfo.getExpired())) && (userInfo.getExpired() < System.currentTimeMillis())) {
            throw new BaseException(ExceptionType.TOKEN_TIME_OUT_ERROR);
        }
        ThreadLocalMap.pull(IgnoreTokenAuthorization.CURRENT_USER,userInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return ;
        }
        //线程处理完后清理，避免OOM
        ThreadLocalMap.remove();
    }
}
