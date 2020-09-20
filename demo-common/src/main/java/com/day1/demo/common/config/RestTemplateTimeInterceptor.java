package com.day1.demo.mapper.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 14:33
 * @Description:
 */
@Component
public class RestTemplateTimeInterceptor implements ClientHttpRequestInterceptor {

    @Value("#{${httpBigTime:1000}}")
    private Long httpBigTime;

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String url = httpRequest.getURI().toString();
        URI uri = httpRequest.getURI();
        //日志
        long start = System.currentTimeMillis();
        try {
            clientHttpRequestExecution.execute(httpRequest,bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > httpBigTime) {
                //日志
//                logger.warn("url执行时间比较长：{};执行时间为：{}毫秒", url, time);
            }
        }
        return null;
    }
}
