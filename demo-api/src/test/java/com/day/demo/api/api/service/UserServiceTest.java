package com.day.demo.api.api.service;

import com.day.demo.api.DemoApiApplicationTests;
import com.day1.demo.api.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: LinHangHui
 * @Date: 2020/10/26 10:32
 */
public class UserServiceTest extends DemoApiApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void buildToken() {
        String token = userService.buildToken(1L);
        Assert.notNull(token);
        System.out.println(token);
    }
}
