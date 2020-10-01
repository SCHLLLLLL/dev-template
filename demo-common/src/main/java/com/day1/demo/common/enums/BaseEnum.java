package com.day1.demo.common.enums;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:29
 * @Description:
 */
public interface BaseEnum {

    /**
     * 获取枚举值
     */
    Integer getCode();

    /**
     * 获取枚举提示
     */
    String getMsg();
    /**
     * 获取枚举描述
     */
    default String getDesc(){
        return getMsg();
    }
    /**
     * 获取枚举实现类
     */
    static <E extends Enum<?> & BaseEnum> E get(Class<E> enumClass, Integer code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getCode().equals(code))
                .findAny().orElse(null);
    }

    /**
     * 获取枚举实现类
     */
    static <E extends Enum<?> & BaseEnum> E get(Class<E> enumClass, String msg) {
        if (StringUtils.isBlank(msg)) {
            return null;
        }
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.getMsg().equalsIgnoreCase(msg))
                .findAny().orElse(null);
    }
}
