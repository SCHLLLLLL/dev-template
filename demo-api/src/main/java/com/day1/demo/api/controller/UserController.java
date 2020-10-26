package com.day1.demo.api.controller;

import com.day1.demo.api.service.UserService;
import com.day1.demo.common.anotation.IgnoreTokenAuthorization;
import com.day1.demo.common.inout.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: LinHangHui
 * @Date: 2020/10/26 11:31
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "测试")
    @IgnoreTokenAuthorization
    @GetMapping("/test")
    public BaseResponse<String> buildToken() {
        String token = userService.buildToken(1L);
        return BaseResponse.valueOfSuccess(token);
    }
}
