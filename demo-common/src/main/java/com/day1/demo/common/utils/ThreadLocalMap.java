package com.day1.demo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: LinHangHui
 * @Date: 2020/9/15 21:50
 * @Description:
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadLocalMap {

    private static final ThreadLocal<Map<String,Object>> THREAD_CONTEXT = new MyThreadLocal();

    public static Map getContext() {
        return THREAD_CONTEXT.get();
    }

    public static void pull(String key, Object value) {
        getContext().put(key,value);
    }

    public static void remove() {
        THREAD_CONTEXT.remove();
    }

    public static Object get(String key) {
        return getContext().get(key);
    }


    private static class MyThreadLocal extends ThreadLocal<Map<String,Object>> {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<String,Object>(8) {
                private static final long serialVersionUID = 3637958959138295593L;

                @Override
                public Object put(String key, Object value) {
                    return super.put(key, value);
                }
            };
        }
    }
}
