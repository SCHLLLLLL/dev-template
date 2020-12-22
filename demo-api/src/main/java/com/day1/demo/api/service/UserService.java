package com.day1.demo.api.service;

import com.day1.demo.common.auth.TokenService;
import com.day1.demo.common.auth.UserInfo;
import com.day1.demo.mapper.model.DsUser;
import com.day1.demo.mapper.service.DsUserService;
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

    private final DsUserService dsUserService;

    public String buildToken(Long userId) {
        DsUser dsUser = dsUserService.getById(userId);
        return buildToken(dsUser);
    }

    public String buildToken(DsUser dsUser) {
        return TokenService.createToken(UserInfo.builder().userId(dsUser.getId()).
                nickName(dsUser.getNickName()).build());
    }

    public DsUser getUserInfo(Long userId) {
        return dsUserService.getById(userId);
    }
}
