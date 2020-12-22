package com.day1.demo.api.controller;

import com.day1.demo.api.service.CommonService;
import com.day1.demo.business.third.SmsService;
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
 * @Date: 2020/11/4 20:36
 */
@Api(tags = "通用请求")
@RequestMapping(value = "/common")
@RestController
public class CommonController {

    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "测试")
    @IgnoreTokenAuthorization
    @GetMapping(value = "/testSendSms")
    public BaseResponse sendSms() {
        commonService.sendSmsCode("13016352863","1234");
        return BaseResponse.valueOfSuccess();
    }
}
