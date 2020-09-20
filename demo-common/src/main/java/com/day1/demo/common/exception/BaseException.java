package com.day1.demo.common.exception;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:39
 * @Description:    BaseException
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -883790721507851239L;

    private int code;

    private String data;

    private String msg;

    public BaseException(int code, String msg) {
        super(msg);
        this.code =code;
        this.msg = msg;
    }

    public BaseException(int code, String msg, String data) {
        super(msg);
        this.code =code;
        this.msg = msg;
        this.data = data;
    }

    public BaseException(IExceptionType exceptionType) {
        super(exceptionType.getMsg());
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.msg = exceptionType.getUserMsg();
    }

    public BaseException(ExceptionType code, String msg) {
        super(msg);
        this.code = code.getCode();
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
