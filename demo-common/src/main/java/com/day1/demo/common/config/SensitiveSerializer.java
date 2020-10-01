package com.day1.demo.common.config;

import com.day1.demo.common.anotation.Sensitive;
import com.day1.demo.common.enums.SensitiveTypeEnum;
import com.day1.demo.common.utils.SensitiveUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 15:09
 * @Description: 数据脱敏
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveTypeEnum sensitiveType;

    public SensitiveSerializer(){}
    public SensitiveSerializer(SensitiveTypeEnum sensitiveType) {
        this.sensitiveType = sensitiveType;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isBlank(s)) {
            jsonGenerator.writeString(s);
            return;
        }

        switch (sensitiveType) {
            case MOBILE_PHONE:
                jsonGenerator.writeString(SensitiveUtils.mobilePhone(s));break;
            case EMAIL:
                jsonGenerator.writeString(SensitiveUtils.email(s));break;
            case ADDRESS:
                jsonGenerator.writeString(SensitiveUtils.address(s,50));
            case BANK_CARD:
                jsonGenerator.writeString(SensitiveUtils.bankCard(s));break;
            case ACCOUNT:
                jsonGenerator.writeString(SensitiveUtils.account(s)); break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (null != beanProperty) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
                Sensitive sensitive = beanProperty.getAnnotation(Sensitive.class);
                if (null == sensitive) {
                    sensitive = beanProperty.getContextAnnotation(Sensitive.class);
                }
                if (null != sensitive) {
                    // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                    return new SensitiveSerializer(sensitive.sensitiveType());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return this;
    }
}
