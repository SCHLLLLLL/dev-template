package com.day1.demo.common.exception;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:42
 * @Description:
 */
public interface IExceptionType {

    /**
     *信息组装
     * @param args  参数
     * @return 信息
     */
    String getMsg(Object... args);

    /**
     * 错误码
     * @return  错误码
     */
    int getCode();

    /**
     * 错误信息
     * @return  错误信息
     */
    String getMsg();

    /**
     * 错误信息
     * @return  错误信息
     */
    String getUserMsg();
}
