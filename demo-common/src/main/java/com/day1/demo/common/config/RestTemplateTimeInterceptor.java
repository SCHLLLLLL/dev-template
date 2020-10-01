package com.day1.demo.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 14:33
 * @Description:
 */
@Component
@Slf4j
public class RestTemplateTimeInterceptor implements ClientHttpRequestInterceptor {

    @Value("#{${httpBigTime:1000}}")
    private Long httpBigTime;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String url = httpRequest.getURI().toString();
        //日志
        long start = System.currentTimeMillis();
        try {
            ClientHttpResponse execute = clientHttpRequestExecution.execute(httpRequest, bytes);
            return execute;
        } catch (Exception e) {
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > httpBigTime) {
                //日志
                log.warn("url执行时间比较长：{};执行时间为：{}毫秒", url, time);
            }
        }
    }
}
