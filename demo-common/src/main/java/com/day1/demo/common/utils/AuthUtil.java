package com.day1.demo.common.utils;


import com.alibaba.fastjson.JSON;
import com.day1.demo.common.auth.UserInfo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author: LinHangHui
 * @Date: 2020/9/15 20:12
 * @Description:
 */
@Slf4j
public class AuthUtil {

    //签名算法
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    //密钥
    private static Key secretKey;

    static {
        try {
            // 加密密文，私钥
            String authSecret = "";
            // 根据给定的字节数组使用加密算法构造一个密钥
            secretKey = new SecretKeySpec(authSecret.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
            log.info(" init AuthUtil ^_^  {}",authSecret);
        } catch (Exception e) {
            log.error("{}",e);
            throw new RuntimeException("load error"+ e.getMessage());
        }
    }

    public static Key getSecretKey(String authSecret) {
        return new SecretKeySpec(authSecret.getBytes(),SIGNATURE_ALGORITHM.getJcaName());
    }

    public static String createToken(UserInfo userInfo) {
        return Jwts.builder().setSubject(String.valueOf(userInfo.getUserId()))
                .claim("body", JSON.toJSONString(userInfo))
                .signWith(SIGNATURE_ALGORITHM,secretKey)
                .compact();
    }

    public static UserInfo parseToken(final String token) {
        if (StringUtils.isBlank(token) || "null".equalsIgnoreCase(token.trim())) {
            return null;
        }
        try {
            Claims claims = Jwts.parser().parseClaimsJwt(token).getBody();
            String body = (String) claims.get("body");
            UserInfo userInfo = JSON.parseObject(body, UserInfo.class);
            return userInfo;
        } catch (Exception e) {
            log.error("用户授权失败，token不合法token：{}，异常：{}",token,e);
            return null;
        }
    }

}
