package com.day1.demo.mapper.common.enums;

import lombok.Getter;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 15:10
 * @Description:
 */
@Getter
public enum SensitiveTypeEnum implements BaseEnum{
    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     * 账号,可能为邮箱或者手机号码
     */
    ACCOUNT;
    ;


    @Override
    public Integer getCode() {
        return this.ordinal();
    }

    @Override
    public String getMsg() {
        return this.toString();
    }

    @Override
    public String getDesc() {
        return this.toString();
    }
}
