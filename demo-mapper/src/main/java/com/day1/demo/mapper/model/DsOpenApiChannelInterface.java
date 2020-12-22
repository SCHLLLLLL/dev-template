package com.day1.demo.mapper.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.day1.demo.mapper.enums.DsOpenApiUriEnum;
import com.day1.demo.mapper.enums.TimeUnitEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 14:15
 */
@Data
public class DsOpenApiChannelInterface {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 渠道数据id
     */
    private Long channelId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(update = "now()")
    private LocalDateTime updateTime;

    /**
     * 接口配置id
     */
    private DsOpenApiUriEnum interfaceUriIndex;

    /**
     * 限流,为0不生效
     */
    private Integer qptLimitTotal;

    /**
     * 限流单位,秒=0;分=1;时=2;天=3
     */
    private TimeUnitEnum timeUnit;
}
