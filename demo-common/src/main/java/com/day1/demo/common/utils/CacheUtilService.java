package com.day1.demo.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author: LinHangHui
 * @Date: 2020/10/27 18:00
 */
@Service
@Slf4j
public class CacheUtilService {

    private static RedisTemplate redisTemplate;
    private static RedissonClient redissonClient;

    @Autowired
    public CacheUtilService(RedisTemplate redisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    public static boolean tryLock(String key, Long liveTime, Long waitTime) {
        try {
            return redissonClient.getLock(key).tryLock(waitTime, liveTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取缓存锁异常:key:{}", key);
        }
        return false;
    }

    public static void releaseLock(String key) {
        try {
            redissonClient.getLock(key).unlock();
        } catch (Exception e) {
            log.error("解锁失败:key:{}", key);
        }
    }

    public static String get(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(obj)) {
            return obj.toString();
        }
        return null;
    }
    public static <T> T get(String cacheKey, Class<T> clazz) {
        Object cache = redisTemplate.opsForValue().get(cacheKey);
        if (cache != null) {
            return JSON.parseObject(cache.toString(), clazz);
        }
        return null;
    }

    public static void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static void set(String key, String value, int timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public static void setJson(String key, Object value, int timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value), timeout, timeUnit);
    }
}
