package com.day1.demo.common.utils;


import com.alibaba.fastjson.JSON;
import com.day1.demo.common.auth.UserInfo;
import com.day1.demo.common.logback.DemoLoggerEnum;
import com.day1.demo.common.logback.log.DemoLogger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Random;

/**
 * @author: LinHangHui
 * @Date: 2020/9/15 20:12
 * @Description:
 */
@Component
public class AuthUtil {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static DemoLogger logger = new DemoLogger(AuthUtil.class);
    private static Key secretKey;

    public static final Random RAN = new Random();

    static {
        try {
            String authSecret = "3d990d2276917dfac04467df11fff26d";
            secretKey = new SecretKeySpec(authSecret.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
            logger.info(" init AuthUtility ^_^  {}", authSecret);
        } catch (Exception e) {
            logger.error(DemoLoggerEnum.GLOBAL_EXCEPTION, "{}", e);
        }
    }


    public static Key getSecretKey(String authSecret) {
        return new SecretKeySpec(authSecret.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
    }

    public static String createToken(UserInfo user) {
        return createToken(user, secretKey);
    }

    public static String createToken(UserInfo user, Key secretKey) {
        return Jwts.builder().setSubject(String.valueOf(user.getUserId()))
                .claim("body", JSON.toJSONString(user)).claim("ran", RAN
                        .nextInt()).signWith(
                        SIGNATURE_ALGORITHM, secretKey)
                .compact();
    }

    public static UserInfo parseToken(String token) {
        if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token.trim())) {
            return null;
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            String body = (String) claims.get("body");
            return JSON.parseObject(body, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.infoNotUser("用户授权失败且token不合法：{},异常：{}", token, e);
            //禁止这里打日志，会造成死循环，需要联系项目负责人
            return null;
        }

    }

}
