package com.day1.demo.common.aop;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

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

    @Value(("log.show:true"))
    private Boolean logShow = true;

    @Pointcut(value = "execution(* com.day1.demo.*.controller..*.*(..))")
    private void webAop() {}

    @Before("webAop()")
    private void doBefore() {
        ELAPSE.set(System.currentTimeMillis());
    }

    @AfterReturning(value = "webAop()", argNames = "joinPoint,object", returning = "object")
    private void doAfterRunning(JoinPoint joinPoint, Object object) {

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
}
