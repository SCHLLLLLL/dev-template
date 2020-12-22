package com.day1.demo.mapper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.day1.demo.common.enums.BaseEnum;
import lombok.Getter;

/**
 * @author nemo.kwok
 * @date 2020/9/8 10:48
 */
@Getter
public enum EnableStatus implements BaseEnum {

    /**
     *
     */
    DISABLE(0, "禁用"),
    ENABLE(1, "开启"),
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    EnableStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getMsg() {
        return this.desc;
    }
}
