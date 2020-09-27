package com.day1.demo.common.anotation;

import com.day1.demo.common.enums.SensitiveTypeEnum;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:45
 * @Description:    数据脱敏
 */

@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize
public @interface Sensitive {
    SensitiveTypeEnum sensitiveType() default SensitiveTypeEnum.MOBILE_PHONE;
}
