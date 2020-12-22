package com.day1.demo.open.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.day1.demo.open.interceptor.SignInterceptor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 15:41
 */
public class SignUtils {

    private final static PropertyFilter JSON_FILER = (source, key, value) -> {
        if (Objects.isNull(value)) {
            return false;
        }
        if (value instanceof String && StringUtils.isBlank(String.valueOf(value))){
            return false;
        }
        return true;
    };

    public static String buildSign(Map<String, Object> params, String secret) {

        Map<String, Object> map = params.entrySet().stream().filter(entry -> !StringUtils.equals(entry.getKey(), SignInterceptor.SIGN))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String json = JSON.toJSONString(map, JSON_FILER, SerializerFeature.MapSortField);
        return DigestUtils.md5Hex(json);
    }
}
