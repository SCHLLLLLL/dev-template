package com.day1.demo.common.inout;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 17:46
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BasePage extends BaseRequest{

    private static final long serialVersionUID = -4094863882434390948L;

    /**
     * 当前页
     */
    @Min(value = 1, message = "currentPage最小1")
    @ApiModelProperty(notes = "第几页", example = "1")
    protected int page = 1;

    /**
     * 每页显示记录数
     */
    @Max(value = 100, message = "每页最大100")
    @ApiModelProperty(notes = "每页大小", example = "10")
    protected int pageSize = 10;
}
