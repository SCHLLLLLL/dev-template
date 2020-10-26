package com.day1.demo.mapper.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author: LinHangHui
 * @Date: 2020/10/24 18:50
 */
public class BaseService<Mp extends BaseMapper<T> ,T> extends ServiceImpl<Mp, T> {
    public LambdaQueryWrapper<T> getWrapper() { return new LambdaQueryWrapper<>();}
}
