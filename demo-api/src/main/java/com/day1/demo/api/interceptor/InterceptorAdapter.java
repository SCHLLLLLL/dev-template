package com.day1.demo.api.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:58
 * @Description:
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterceptorAdapter implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> patters = new ArrayList<>();
        patters.add("/swagger-resources/**");
        patters.add("/error");
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns(patters);
    }
}
