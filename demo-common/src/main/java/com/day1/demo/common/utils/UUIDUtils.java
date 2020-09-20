package com.day1.demo.common.utils;

import java.util.UUID;

/**
 * @author: LinHangHui
 * @Date: 2020/9/16 21:09
 * @Description:
 */
public class UUIDUtils {

    public static final String uuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
