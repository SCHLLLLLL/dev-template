package com.day1.demo.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author: LinHangHui
 * @Date: 2020/9/14 21:04
 * @Description: 用mapping映射的方式在使用自定义interceptor拦截器时会失效
 * 原因是请求经过的先后顺序问题，当请求到来时会先进入拦截器中，
 * 而不是进入Mapping映射中，所以返回的头信息中并没有配置的跨域信息。浏览器就会报跨域异常。
 * 跨域原理就是在响应头上加字段
 */
@Configuration
public class FilterCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Access-Control-Allow-Origin:"*"
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);

        CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
        //设置过滤器的顺序
        bean.setOrder(0);
        return corsFilter;
    }
}
