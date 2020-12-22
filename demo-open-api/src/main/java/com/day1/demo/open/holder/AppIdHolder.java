package com.day1.demo.open.holder;

import com.day1.demo.common.utils.ThreadLocalMap;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 11:47
 */
public class AppIdHolder {

    private static final String APP_ID = "APP_ID";

    public static String get() {
        return (String) ThreadLocalMap.get(APP_ID);
    }

    public static void set(String appId) {
        ThreadLocalMap.pull(APP_ID, appId);
    }

    public static void remove() {
        ThreadLocalMap.remove(APP_ID);
    }
}
