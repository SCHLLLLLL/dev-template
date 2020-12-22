package com.day1.demo.common.anotation.handle;

import com.day1.demo.common.anotation.CustomDemo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author: LinHangHui
 * @Date: 2020/12/22 22:19
 * 这只是个demo，可以通过实现ConstraintValidator接口来自定义验证注解
 * 泛型为<自定义注解, 注解标注类型>
 */
public class CustomValidator implements ConstraintValidator<CustomDemo, String> {

    /**
     * 初始化
     */
    @Override
    public void initialize(CustomDemo constraintAnnotation) {

    }

    /**
     * 进行一些自定义操作
     * 参数为： 注解标注的值
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        //进行自定义校验逻辑
        return false;
    }
}
