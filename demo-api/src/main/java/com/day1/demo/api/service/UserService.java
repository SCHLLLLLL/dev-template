package com.day1.demo.api.service;

import com.day1.demo.common.auth.TokenService;
import com.day1.demo.common.auth.UserInfo;
import com.day1.demo.mapper.model.DMUser;
import com.day1.demo.mapper.service.DmUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: LinHangHui
 * @Date: 2020/9/16 15:43
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final DmUserService dmUserService;

    public String buildToken(Long userId) {
        DMUser dmUser = dmUserService.getById(userId);
        return buildToken(dmUser);
    }

    public String buildToken(DMUser dmUser) {
        return TokenService.createToken(UserInfo.builder().userId(dmUser.getId()).
                nickName(dmUser.getNickName()).build());
    }
}
