package com.day1.demo.mapper.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: LinHangHui
 * @Date: 2020/10/24 18:41
 */
@Data
@TableName(value = "demo_user")
public class DMUser implements Serializable {

    private static final long serializableUID = 129878173650031594L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String nickName;
}
