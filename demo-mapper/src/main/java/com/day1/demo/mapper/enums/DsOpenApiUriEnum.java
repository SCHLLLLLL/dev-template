package com.day1.demo.mapper.enums;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.day1.demo.common.enums.BaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author nemo.kwok
 * @date 2020/9/8 10:48
 */
@Getter
public enum DsOpenApiUriEnum implements BaseEnum {

    /**
     * TODO:后续换成数据库配置
     */
    wmOrderCreate(0, "/demo-open-api/meituan/order/v1/create", "创建美团外卖会员订单"),
    wmOrderQueryList(1, "/demo-open-api/meituan/order/v1/query", "美团外卖会员订单查询"),
    getOrderStockLeft(2, "/demo-open-api/meituan/order/v1/stock", "美团外卖会员剩余下单次数"),

    testGetSign(100, "/demo-open-api/meituan/test/testGetSign", "测试方法"),
    testPostSign(101, "/demo-open-api/meituan/test/testPostSign", "测试方法"),
    testOrderCallback(102, "/demo-open-api/meituan/test/order/callback", "测试方法"),
    ;

    @EnumValue
    private final Integer code;
    private final String apiUri;
    private final String apiName;

    DsOpenApiUriEnum(Integer code, String apiUri, String apiName) {
        this.code = code;
        this.apiUri = apiUri;
        this.apiName = apiName;
    }

    @Override
    public String getMsg() {
        return this.apiName;
    }

    public static DsOpenApiUriEnum getByApiUri(String apiUri) {
        return Arrays.stream(DsOpenApiUriEnum.values()).filter(a -> StringUtils.equals(a.apiUri, apiUri)).findAny()
            .orElse(null);

    }

}
