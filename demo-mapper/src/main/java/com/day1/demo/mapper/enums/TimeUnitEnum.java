package com.day1.demo.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.day1.demo.common.enums.BaseEnum;
import lombok.Getter;

/**
 * @author nemo.kwok
 * @date 2020/9/8 10:48
 */
@Getter
public enum TimeUnitEnum implements BaseEnum {

    /**
     *
     */
    SECOND(0, "秒"),

    MINUTE(1, "分"),

    HOUR(2, "时"),

    DAY(3, "天"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    TimeUnitEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getMsg() {
        return this.desc;
    }
}
