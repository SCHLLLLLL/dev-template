package com.day1.demo.common.logback;

/**
 * @author: LinHangHui
 * @Date: 2020/9/15 21:32
 * @Description:
 * 需封装logback使用
 */
public enum DemoLoggerEnum {
    /**
     *
     */
    INFO("普通日志"),
    /**
     *
     */
    TOKEN_EXCEPTION("token生成异常"),
    /**
     *
     */
    DB_ERROR("数据库操作异常"),
    /**
     *
     */
    SYS_LOG_EXCEPTION("系统日志处理异常"),
    /**
     *
     */
    THIRD_EXCEPTION("第三方接口处理异常"),
    /**
     *
     */
    GLOBAL_EXCEPTION("全局异常处理"),
    ;

    public String title;
    DemoLoggerEnum(String title) {this.title = title;}
}
