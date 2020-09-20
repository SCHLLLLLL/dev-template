package com.day1.demo.common.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: LinHangHui
 * @Date: 2020/9/15 20:07
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {

    @ApiModelProperty("用户Id")
    private Long userId;

    @ApiModelProperty("Token失效时间")
    private Long expired;
}
