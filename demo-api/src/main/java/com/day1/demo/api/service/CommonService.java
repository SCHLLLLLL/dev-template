package com.day1.demo.api.service;

import com.alibaba.fastjson.JSONObject;
import com.day1.demo.business.third.SmsService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: LinHangHui
 * @Date: 2020/11/4 20:40
 */
@Service
public class CommonService {

    @Setter(onMethod = @__(@Autowired))
    private SmsService smsService;

    public boolean sendSmsCode(String phone, String code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        return smsService.sendSmsCode(phone, jsonObject.toJSONString());
    }
}
