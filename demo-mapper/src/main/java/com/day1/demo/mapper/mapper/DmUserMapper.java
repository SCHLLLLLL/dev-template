package com.day1.demo.mapper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.day1.demo.mapper.model.DMUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: LinHangHui
 * @Date: 2020/10/24 18:49
 */
@Mapper
public interface DmUserMapper extends BaseMapper<DMUser> {
}
