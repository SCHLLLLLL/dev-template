package com.day1.demo.common.exception;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:40
 * @Description:
 */
public enum ExceptionType implements IExceptionType{
    JAVA_SUCCESS(200,"200","200"),

    SUCCESS(0,"success","success"),
    BUSINESS_ERROR(-1, "常规业务错误", "常规业务错误"),
    PERMISSION_DENIED(999, "没有权限", "没有权限"),

    SYSTEM_ERROR(500, "系统内部异常", "系统内部异常"),

    SYSTEM_BUSY_ERROR(590, "系统繁忙，请稍后重试", "系统繁忙，请稍后重试"),

    SIGN_INVLID_ERROR(501, "签名错误", "签名错误"),

    TOKEN_TIME_OUT_ERROR(502, "token已经过期", "token已经过期"),

    TOKEN_INVLID_ERROR(503, "token不合法", "非法操作"),

    OPERATE_ERROR(507, "非法操作", "非法操作"),

    OPERATE_FAIL(508, "操作失败", "操作失败"),

    PARAM_TYPE_ERROR(509, "数据格式错误", "数据格式错误"),

    PARAM_INVALID(509, "参数校验失败", "参数校验失败"),

    NET_WORK_ERROR(510, "网络异常,请稍后重试", "网络异常,请稍后重试"),

    ;


    private int code;

    private String msg;

    private String userMsg;

    ExceptionType(int code, String msg, String userMsg) {
        this.code = code;
        this.msg = msg;
        this.userMsg = userMsg;
    }

    @Override
    public String getMsg(Object... args) {
        if (args == null || args.length == 0) {
            return msg;
        }
        FormattingTuple ft = MessageFormatter.arrayFormat(msg, args);
        return ft.getMessage();
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getUserMsg() {
        return userMsg;
    }
}
