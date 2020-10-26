package com.day1.demo.common.inout;

import com.day1.demo.common.exception.BaseException;
import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.exception.IExceptionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Day1
 * @Date: 2020/9/14 11:59
 * @Description:
 */
@Data
public class BaseResponse<T> {

    /**
     * 错误码
     */
    @ApiModelProperty(notes = "0表示成功，其他表示错误", example = "0")
    private int code = ExceptionType.JAVA_SUCCESS.getCode();
    /**
     * 提示信息
     */
    @ApiModelProperty(notes = "提示信息")
    private String msg = "success";

    @ApiModelProperty(notes = "服务器时间", example = "1542183561830")
    private Long timestamp;

    @ApiModelProperty(notes = "调用成功时返回的数据")
    private T data;


    @JsonIgnore
    public boolean isSuccess(ExceptionType exceptionType) {
        return this.code == exceptionType.getCode();
    }

    public static <T>BaseResponse<T> valueOfSuccess(T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setData(data);
        baseResponse.setTimestamp(System.currentTimeMillis());
        return baseResponse;
    }

    public static <T>BaseResponse<T> valueOfSuccess(String msg) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setMsg(msg);
        baseResponse.setTimestamp(System.currentTimeMillis());
        return baseResponse;
    }

    public static <T>BaseResponse<T> valueOfError(IExceptionType exceptionType) {
        return valueOfError(exceptionType.getCode(),exceptionType.getMsg());
    }

    public static <T>BaseResponse<T> valueOfError(BaseException baseException) {
        return valueOfError(baseException.getCode(),baseException.getMsg());
    }

    public static <T>BaseResponse<T> valueOfError(int code, String msg) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(code);
        baseResponse.setMsg(msg);
        baseResponse.setTimestamp(System.currentTimeMillis());
        return baseResponse;
    }

    public static <T> BaseResponse<T> valueOfError(ExceptionType code, String msg) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setTimestamp(System.currentTimeMillis());
        baseResponse.setCode(code.getCode());
        baseResponse.setMsg(msg);
        return baseResponse;
    }

}
