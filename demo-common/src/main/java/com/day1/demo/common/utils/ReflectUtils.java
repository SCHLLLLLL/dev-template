package com.day1.demo.common.utils;

import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.logback.DemoLoggerEnum;
import com.day1.demo.common.logback.log.DemoLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: LinHangHui
 * @Date: 2020/12/22 17:54
 * 反射工具类
 */
public class ReflectUtils {

    private final static DemoLogger logger = new DemoLogger(ReflectUtils.class);

    /**
     * 获取get方法的方法名
     */
    public static String generateGetMethodName(String filedName) {
        return "get" + filedName.substring(0,1).toUpperCase() + filedName.substring(1);
    }

    /**
     * 获取set方法的方法名
     */
    public static String generateSetMethodName(String filedName) {
        return "set" + filedName.substring(0,1).toUpperCase() + filedName.substring(1);
    }

    /**
     * 获取bean的class名称
     */
    public static String generateBeanName(Class<?> clazz) {
        String className = clazz.getSimpleName();
        return className.substring(0,1).toLowerCase() + className.substring(1);
    }
    /**
     * 获取get方法
     */
    public static Method generateGetMethod(Class<?> clazz, String fieldName) {
        Method method;
        try {
            method = clazz.getMethod(generateGetMethodName(fieldName));
        } catch (NoSuchMethodException e) {
            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "反射错误:{}", e);
            throw new BaseException(ExceptionType.OPERATE_FAIL);
        }
        return method;
    }
    /**
     * 获取set方法
     */
    public static Method generateSetMethod(Class<?> clazz, String fieldName, Class<?> parameterTypes) {
        Method method;
        try {
            method = clazz.getMethod(generateSetMethodName(fieldName), parameterTypes);
        } catch (NoSuchMethodException e) {
            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "反射错误:{}", e);
            throw new BaseException(ExceptionType.OPERATE_FAIL);
        }
        return method;
    }

    /**
     * 执行set方法
     */
    public static void invokeGetMethod(Object o, String fieldName) {
        Method method = generateGetMethod(o.getClass(), fieldName);
        try {
            method.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "反射错误:{}", e);
            throw new BaseException(ExceptionType.OPERATE_FAIL);
        }
    }

    /**
     * 执行set方法
     */
    public static void invokeSetMethod(Object o, String fieldName, Object args) {
        Method method = generateSetMethod(o.getClass(), fieldName, args.getClass());
        try {
            method.invoke(o, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "反射错误:{}", e);
            throw new BaseException(ExceptionType.OPERATE_FAIL);
        }
    }

    public static String getCurrentWholeMethodName() {
        try {
            String className = Thread.currentThread().getStackTrace()[2].getClassName();
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            return String.format("%s.%s", className, methodName);
        } catch (Exception ex) {
            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "获取当前方法名字异常,error:{}", ex);
            return UUIDUtils.uuid();
        }
    }
}
