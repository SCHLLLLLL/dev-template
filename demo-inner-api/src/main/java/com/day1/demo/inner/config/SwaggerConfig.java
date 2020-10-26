package com.day1.demo.inner.config;

import com.day1.demo.common.exception.ExceptionType;
import com.day1.demo.common.exception.IExceptionType;
import com.day1.demo.inner.interceptor.TokenInterceptor;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author: Gordon
 * @Date: 2020/9/14 10:55
 * @Description:
 */

@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {

    @Value("${swagger.enabled:true}")
    private boolean isSwaggerEnabled;

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("app接口文档 ").description("app接口文档")
                .version("1.0").build();
    }

    @Bean
    public Docket api() {
        List<IExceptionType> exceptionTypeList = new ArrayList<>();
        List<ResponseMessage> responseMessageList = new ArrayList<>(responseMessageList(exceptionTypeList));
        return commonDocket(responseMessageList, "", "default");
    }

    private Set<ResponseMessage> responseMessageList(List<IExceptionType> exceptionTypeList) {
        if (CollectionUtils.isEmpty(exceptionTypeList)) {
            exceptionTypeList = new ArrayList<>();
        }
        exceptionTypeList.addAll(new ArrayList<>(Arrays.asList(ExceptionType.values())));
        return exceptionTypeList.parallelStream().map(errorEnums -> {
            return new ResponseMessageBuilder().code(errorEnums.getCode()).message(errorEnums.getMsg()).responseModel(
                    new ModelRef(errorEnums.getMsg())).build();
        }).collect(Collectors.toSet());
    }

    private List<Parameter> getSwaggerParam() {
        List<Parameter> params = new ArrayList<Parameter>();
        params.add(new ParameterBuilder()
                .name(TokenInterceptor.TOKEN_HEADER_NAME)
                .description(TokenInterceptor.TOKEN_HEADER_NAME)
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false)
                .build());
        return params;
    }

    private Docket commonDocket(List<ResponseMessage> responseMessageList, String pathRegex, String groupName) {
        return new Docket(DocumentationType.SWAGGER_2)
                // 添加全局响应状态码
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo())
                .enable(isSwaggerEnabled)
                .select()
                .apis(Predicates.and(RequestHandlerSelectors.basePackage("com.day1.demo"), RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)))
                .paths(StringUtils.isEmpty(pathRegex) ? PathSelectors.any() : regex(pathRegex)).build().globalOperationParameters(getSwaggerParam())
                .groupName(groupName).pathMapping("/");
    }

}
