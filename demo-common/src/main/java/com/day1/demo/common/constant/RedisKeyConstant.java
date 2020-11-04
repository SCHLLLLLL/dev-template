package com.day1.demo.common.constant;

/**
 * @author: LinHangHui
 * @Date: 2020/10/27 17:52
 */
public class RedisKeyConstant {

    private final static String PREFIX = "demo:";

    private final static String ATOMIC_TYPE = "atomic:";

    private final static String HASH_TYPE = "hash:";

    private final static String LIST_TYPE = "list:";

    private final static String BASE_TYPE = "base:";

    private final static String SET_TYPE = "set:";

    private final static String ZSET_TYPE = "zset:";

    private final static String DICTIONARY = PREFIX + "dictionary:%s";

    private final static String REDISSON_LOCK = PREFIX + "redisson_lock:{}";
}
