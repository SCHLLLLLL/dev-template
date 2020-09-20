package com.day1.demo.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:53
 * @Description:
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许跨域访问的路径
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("*")    // 允许跨域访问的源
                .allowCredentials(true) // 是否发送cookie
                .maxAge(3600);  // 指定预检请求的有效期
    }
}
