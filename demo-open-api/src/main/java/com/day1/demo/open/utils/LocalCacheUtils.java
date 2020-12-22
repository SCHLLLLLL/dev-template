package com.day1.demo.open.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 15:02
 */
public class LocalCacheUtils {

    private final static Cache<String, Object> LOCAL_CACHE;

    static {
        LOCAL_CACHE = CacheBuilder.newBuilder().
                maximumSize(100).
                expireAfterAccess(10, TimeUnit.SECONDS).
                concurrencyLevel(Runtime.getRuntime().availableProcessors()).
                build();
    }

    public static Object get(String key) {
        return LOCAL_CACHE.getIfPresent(key);
    }

    public static void put(String key, Object value) {
        LOCAL_CACHE.put(key, value);
    }

    public static void remove(String key) {
        LOCAL_CACHE.invalidate(key);
    }
}
