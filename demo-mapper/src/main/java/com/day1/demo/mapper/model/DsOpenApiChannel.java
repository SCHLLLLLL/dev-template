package com.day1.demo.mapper.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.day1.demo.mapper.enums.EnableStatus;
import com.day1.demo.mapper.enums.TimeUnitEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: LinHangHui
 * @Date: 2020/12/10 14:10
 */
@Data
public class DsOpenApiChannel {

    /**
     * 渠道数据id
     */
    @TableId(type = IdType.AUTO)
    private Long channelId;

    /**
     * 对外渠道id,可自定义,唯一
     */
    private String appId;

    /**
     * 渠道密钥
     */
    private String appKey;

    /**
     * 渠道名称
     */
    private String chanelName;

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
     * 是否可用,是=1
     */
    private EnableStatus enabled;

    /**
     * 总限流,为0不生效
     */
    private Integer qptLimitTotal;

    /**
     * 限流单位,秒=0;分=1;时=2;天=3
     */
    private TimeUnitEnum timeUnit;
}
