package com.day1.demo.common.constant;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author: LinHangHui
 * @Date: 2020/10/27 17:52
 */
public class RedisKeyConstant {

    public final static String PREFIX = "demo:";

    public final static String OPEN_PREFIX = "demo:open:";

    private final static String ATOMIC_TYPE = "atomic:";

    private final static String HASH_TYPE = "hash:";

    private final static String LIST_TYPE = "list:";

    private final static String BASE_TYPE = "base:";

    private final static String SET_TYPE = "set:";

    private final static String ZSET_TYPE = "zset:";

    private final static String DICTIONARY = PREFIX + "dictionary:%s";

    private final static String REDISSON_LOCK = PREFIX + "redisson_lock:{}";

    public static String getLockName(String lockName) {
        return embedKey(REDISSON_LOCK, lockName);
    }


    private final static String OPEN_API_CHANNEL_API_BO_CACHE_KEY =
            OPEN_PREFIX + BASE_TYPE + "open_api_channel_api_bo_cache_key:%s";

    /**
     * 渠道
     */
    private final static String OPEN_API_QPT_LIMIT_FOR_TOTAL =
            OPEN_PREFIX + BASE_TYPE + "open_api_qpt_limit_for_total:%s";

    private final static String OPEN_API_QPT_BAN_FOR_TOTAL =
            OPEN_PREFIX + BASE_TYPE + "open_api_qpt_ban_for_total:%s";

    /**
     * 接口
     */
    private final static String OPEN_API_QPT_LIMIT_FOR_INTERFACE =
            OPEN_PREFIX + BASE_TYPE + "open_api_qpt_limit_for_total:%s:%s";

    private final static String OPEN_API_QPT_BAN_FOR_INTERFACE =
            OPEN_PREFIX + BASE_TYPE + "open_api_qpt_ban_for_interface:%s:%s";




    
    public static String getOpenApiChannelApiBoCacheKey(String appId) {
        return String.format(OPEN_API_CHANNEL_API_BO_CACHE_KEY, appId);
    }










    public static String embedKey(String key, Object... args) {
        if (args == null || args.length == 0) {
            return key;
        }
        FormattingTuple ft = MessageFormatter.arrayFormat(key, args);
        return ft.getMessage();
    }
}
