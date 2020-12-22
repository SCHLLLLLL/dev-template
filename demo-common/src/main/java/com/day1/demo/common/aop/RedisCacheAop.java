package com.day1.demo.common.aop;

import com.alibaba.fastjson.JSON;
import com.day1.demo.common.anotation.RedisCache;
import com.day1.demo.common.constant.RedisKeyConstant;
import com.day1.demo.common.logback.DemoLoggerEnum;
import com.day1.demo.common.logback.log.DemoLogger;
import com.day1.demo.common.utils.CacheUtilService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author nemo.kwok
 * @date 2020/9/11 12:23
 */
@Component
@Aspect
@Slf4j
@Order(3)
public class RedisCacheAop {

    private final String doubleCacheKey = ":copy";

    private final DemoLogger logger = new DemoLogger(getClass());

    private final String splitStr = ":";

    private final String placeholder = "#";

    private static final ThreadLocal<String> REDIS_CACHE_KEY = new ThreadLocal<>();

    @Pointcut("@annotation(com.day1.demo.common.anotation.RedisCache)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取查询的key 连接点是在方法上的,所以可以强转成方法的签名信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取被注解注释的方法
        Method method =
                joinPoint.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes());
        RedisCache rdsCache = method.getAnnotation(RedisCache.class);
        if (null == rdsCache) {
            return joinPoint.proceed();
        }
        // 获取注解中的key
        String key = rdsCache.key();
        StringBuilder pre = new StringBuilder(rdsCache.prefix());
        if (StringUtils.isBlank(pre)) {
            pre.append(rdsCache.value());
        }
        if (StringUtils.isBlank(pre)) {
            pre.append(RedisKeyConstant.PREFIX);
        }

        // 获取方法里的参数值
        Object[] args = joinPoint.getArgs();
        if (StringUtils.isNotBlank(key)) {
            if (key.startsWith(placeholder)) {
                // 获取方法真实参数名
                ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                if (null != parameterNames && parameterNames.length > 0) {
                    // 通过注解中的el表达式获取参数中的值
                    // 1.创建表达式
                    SpelExpressionParser parser = new SpelExpressionParser();
                    Expression expression = parser.parseExpression(key);
                    // 2.设置解析上下文(有哪些占位符以及占位符的值)
                    EvaluationContext context = new StandardEvaluationContext(joinPoint.getArgs());
                    // 将方法的参数名和参数值设置在解析器里
                    for (int i = 0; i < Objects.requireNonNull(parameterNames).length; i++) {
                        context.setVariable(parameterNames[i], args[i]);
                    }
                    // 获取将占位符替换成真实参数后的key
                    key = Objects.requireNonNull(expression.getValue(context)).toString();
                }
            }
        } else {
            key = DigestUtils.md5DigestAsHex(Arrays.toString(args).getBytes());
        }

        if (rdsCache.isEmbedKey()) {
            key = RedisKeyConstant.embedKey(pre.toString(), key);
        } else {
            if (pre.lastIndexOf(splitStr) != pre.length() - 1) {
                pre.append(splitStr);
            }
            key = pre.append(((MethodSignature) joinPoint.getSignature()).getMethod().getName()).append(splitStr)
                    .append(key).toString();
        }
        REDIS_CACHE_KEY.set(key);
        logger.info("RdsCache aop working=======>method={},key={}",
                ((MethodSignature) joinPoint.getSignature()).getMethod().getName(), key);
        // 如果注解用于删除,则直接跳到执行后
        if (rdsCache.doRemoveCache()) {
            return joinPoint.proceed();
        }
        String objectStr = CacheUtilService.get(key);
        if (StringUtils.isNotBlank(objectStr)) {
            return JSON.parseObject(objectStr, method.getGenericReturnType());
        }
        // 回源锁
        String lockKey = RedisKeyConstant.getLockName(key);
        if (rdsCache.doubleCache()) {
            objectStr = CacheUtilService.get(key + doubleCacheKey);
            if (StringUtils.isNotBlank(objectStr)) {
                // 异步回源
                if (CacheUtilService.tryLock(lockKey, rdsCache.lockLive())) {
                    String finalKey = key;
                    CompletableFuture.runAsync(() -> {
                        try {
                            renew(finalKey, rdsCache, method.invoke(joinPoint.getTarget(), joinPoint.getArgs()));
                        } catch (Throwable throwable) {
                            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "error:{}", throwable);
                        } finally {
                            CacheUtilService.releaseLock(lockKey);
                        }
                    }).exceptionally(throwable -> {
                        logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "error:{}", throwable);
                        return null;
                    });
                }
                return JSON.parseObject(objectStr, method.getGenericReturnType());
            }
        }
        // 同步回源
        if (CacheUtilService.tryLock(lockKey, rdsCache.lockLive())) {
            Object object = renew(key, rdsCache, joinPoint.proceed());
            CacheUtilService.releaseLock(lockKey);
            return object;
        }
        return joinPoint.proceed();
    }

    /**
     * 更新回源数据
     *
     * @param key
     * @param rdsCache
     * @param resp
     * @return
     */
    private Object renew(String key, RedisCache rdsCache, Object resp) {
        logger.info("RdsCache=======>renew cache data:key={},resp={}", key, resp);
        if (rdsCache.noNullCache() && null == resp) {
            return null;
        }
        CacheUtilService.setJson(key, resp, rdsCache.live(), rdsCache.TIME_UNIT());
        if (rdsCache.doubleCache()) {
            CacheUtilService.setJson(key + doubleCacheKey, resp, rdsCache.live() * 2, rdsCache.TIME_UNIT());
        }
        return resp;
    }

    /**
     * 缓存注解收尾工作
     *
     * @param joinPoint
     */
    @AfterReturning(value = "pointCut()", argNames = "joinPoint", returning = "joinPoint")
    public void afterReturn(JoinPoint joinPoint) {
        // 获取查询的key 连接点是在方法上的,所以可以强转成方法的签名信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取被注解注释的方法
        Method method = null;
        try {
            method = joinPoint.getTarget().getClass().getMethod(signature.getName(),
                    signature.getMethod().getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String key = REDIS_CACHE_KEY.get();
        REDIS_CACHE_KEY.remove();
        if (null == method) {
            return;
        }
        RedisCache rdsCache = method.getAnnotation(RedisCache.class);
        if (null == rdsCache) {
            return;
        }
        // 清缓存
        if (rdsCache.doRemoveCache()) {
            logger.info("RdsCache=======>remove cache data:key={}", key);
            if (StringUtils.isNotBlank(key)) {
                CacheUtilService.delKey(key);
            }
        }
    }

}
