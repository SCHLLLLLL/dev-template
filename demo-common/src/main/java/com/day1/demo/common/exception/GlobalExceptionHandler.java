package com.day1.demo.common.exception;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.day1.demo.common.inout.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.day1.demo.common.exception.ExceptionType.BUSINESS_ERROR;


/**
 * @author: Day1
 * @Date: 2020/9/14 10:42
 * @Description:
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements HandlerExceptionResolver{

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse exception(Exception exception) {

        return BaseResponse.valueOfError(500,"系统内部异常");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse baseException(BaseException baseException) {
        //日志
        return BaseResponse.valueOfError(baseException);
    }

    /**
     * 请求参数校验异常validated
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse bindException(BindException bindException) {
        StringBuilder errorInfo = new StringBuilder();
        bindException.getAllErrors().forEach(e -> {
            errorInfo.append(e.getDefaultMessage()).append(",");
        });
        return BaseResponse.valueOfError(BUSINESS_ERROR,errorInfo.toString());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ExcelAnalysisException.class)
    @ResponseBody
    public BaseResponse<Void> excelAnalysisExceptionHandler(ExcelAnalysisException ex) {
//        LOGGER.error(KncLoggerEnum.GLOBAL_EXCEPTION, "excel异常：{}", ex);
        return BaseResponse.valueOfError(BUSINESS_ERROR, ex.getMessage());
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(null);
        return modelAndView;
    }
}
