package com.day1.demo.common.aop;

import com.alibaba.fastjson.JSON;
import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.inout.BaseRequest;
import com.day1.demo.common.inout.BaseResponse;
import com.day1.demo.common.logback.DemoLoggerEnum;
import com.day1.demo.common.logback.utils.CheckParamUtils;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LinHangHui
 * @Date: 2020/10/1 3:23
 */

@Aspect
@Component
@Order(2)
@Slf4j
public class ControllerAop {

    //限流
    private static final RateLimiter rateLimiter = RateLimiter.create(3000);

    private static final ThreadLocal<Long> ELAPSE = new ThreadLocal<>();

    @Value(value = "${log.show:true}")
    private boolean logShow;

    @Pointcut(value = "execution(* com.day1.demo.*.controller..*.*(..))")
    private void webAop() {}

    @Before("webAop()")
    private void doBefore() {
        ELAPSE.set(System.currentTimeMillis());
    }

    @AfterReturning(value = "webAop()", argNames = "joinPoint,retVl", returning = "retVl")
    private void doAfterRunning(JoinPoint joinPoint, Object retVl) {
        if (!logShow) {
            return;
        }
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            if (sra == null) {
                return;
            }
            HttpServletRequest request = sra.getRequest();
            Class<?> clazz = joinPoint.getTarget().getClass();
            Method method = this.getMethod(joinPoint);
            Map<String, String> map = this.getHeadersInfo(request);
            StringBuffer sb = new StringBuffer("\n");

            sb.append("请求类:").append(clazz.getName()).append("\n");
            sb.append("请求方法:").append(method.getName()).append("\n");
            sb.append("请求head:").append(JSON.toJSONString(map)).append("\n");
            Object[] params = joinPoint.getArgs();
            if (params.length != 0) {
                for (Object object : params) {
                    if (object instanceof BaseRequest) {
                        sb.append("请求参数").append(JSON.toJSONString(object)).append("\n");
                    }
                }
            }
            sb.append("返回参数:").append(JSON.toJSONString(retVl)).append("\n");
            sb.append("响应时间:").append(System.currentTimeMillis() - ELAPSE.get()).append("\n");
            log.info("\n请求日志：{}", sb);
        } catch (Exception e) {
            log.warn("内部异常:{},{}", e.getMessage(), e);
        }
    }

    @Around("webAop()")
    private Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (!rateLimiter.tryAcquire()) {
            return BaseResponse.valueOfError(ExceptionType.SYSTEM_BUSY_ERROR);
        }

        Object logParam = null;
        Object result = null;
        Object[] params = proceedingJoinPoint.getArgs();
        try {
            for (Object param : params) {
                if (param instanceof Serializable) {
                    logParam = param;
                }
                if (param instanceof BaseRequest) {
                    List<String> errors = CheckParamUtils.check(param);
                    if (errors != null) {
                        String message = errors.toString();
                        log.warn("接口访问失败:{}", message);
                        return BaseResponse.valueOfError(ExceptionType.PARAM_INVALID);
                    }
                }
            }
            result = proceedingJoinPoint.proceed();
        } catch (BaseException e) {
            return BaseResponse.valueOfError(e);
        } catch (DataAccessException de) {
            log.error(DemoLoggerEnum.DB_ERROR + "原因：{}，\n报文：{}", de.getMessage(), de);
            return BaseResponse.valueOfError(ExceptionType.SYSTEM_ERROR);
        } catch (IllegalArgumentException illegalArgumentException) {
        }
        return result;
    }

    public Method getMethod(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        return signature.getMethod();
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(16);
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
