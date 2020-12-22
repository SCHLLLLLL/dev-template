package com.day1.demo.common.utils;

import java.util.Random;
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

    public static String generateRandomNumber(long size) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
