package com.day1.demo.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:45
 * @Description:    忽略token校验
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreTokenAuthorization {
    /**
     * 存储当前用户userId的字段名
     */
    String CURRENT_USER = "CURRENT_USER";

    boolean isEnabled() default false;
}
