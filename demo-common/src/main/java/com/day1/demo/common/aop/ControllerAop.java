package com.day1.demo.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.inout.BaseRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:44
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class ControllerAop {

    @Value("${log.show:false")
    private boolean logShow;

    @Pointcut(value = "execution(* com.day1.demo.*.controller..*.*(..))")
    public void webAop() {
    }

    @AfterReturning(value = "webAop()", argNames = "point,retV1", returning = "retV1")
    public void doAfterRunning(JoinPoint point, Object retV1) {
        if (!logShow) {
            return;
        }
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra == null) {
                return;
            }
            HttpServletRequest request = sra.getRequest();
            Class<?> clazz = point.getTarget().getClass();
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            Map<String, String> headersInfo = this.getHeadersInfo(request);
            StringBuffer sb = new StringBuffer();
            sb.append("请求类：").append(clazz.getName()).append("\n");
            sb.append("请求方法：").append(method.getName()).append("\n");
            sb.append("请求head：").append(JSON.toJSONString(headersInfo)).append("\n");
            Object[] params = point.getArgs();
            if (params.length != 0) {
                for (Object object : params) {
                    if (object instanceof BaseRequest) {
                        sb.append("请求参数：").append(JSON.toJSONString(object)).append("\n");
                    }
                }
            }
            sb.append("返回参数:").append(JSON.toJSONString(retV1)).append("\n");
            log.info("请求类日志：{}",sb);
        } catch (Exception e) {
            log.warn("内部异常:{},{}", e.getMessage(), e);
        }
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
