package com.day1.demo.common.logback.utils;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author: LinHangHui
 * @Date: 2020/10/1 3:47
 */
@Slf4j
public class CheckParamUtils {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static List<String> check(Object entity) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(entity);
        ArrayList<String> list = new ArrayList<>();
        for (ConstraintViolation<Object> constraintViolation : constraintViolationSet) {
            String message = constraintViolation.getMessage();
            list.add(message);
            log.info(message);
        }
        return list;
    }
}
