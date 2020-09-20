package com.day1.demo.common.auth;

import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.utils.AuthUtil;
import com.day1.demo.common.utils.ThreadLocalMap;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 16:05
 * @Description:    token令牌工具类
 */
public class TokenService {

    @Value("${token.header:token}")
    public static String authHeader;

    public static String CURRENT_USER = "CURRENT_USER";

    public static UserInfo getUserInfoByToken(String token) {
        try {
            UserInfo userInfo = AuthUtil.parseToken(token);
            if (userInfo == null) {
                throw new BaseException(ExceptionType.TOKEN_INVLID_ERROR);
            }
            return userInfo;
        } catch (Exception e) {
            throw new BaseException(ExceptionType.TOKEN_INVLID_ERROR);
        }
    }

    public static String createToken(UserInfo userInfo) {
        return AuthUtil.createToken(userInfo);
    }

    public static UserInfo getUserInfo() {
        return (UserInfo) ThreadLocalMap.get(CURRENT_USER);
    }
}
