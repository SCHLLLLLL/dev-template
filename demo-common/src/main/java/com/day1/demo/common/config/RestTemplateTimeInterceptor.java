package com.day1.demo.common.config;

import com.day1.demo.common.logback.log.DemoLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 14:33
 * @Description: rest请求拦截器
 */
@Component
public class RestTemplateTimeInterceptor implements ClientHttpRequestInterceptor {

    private DemoLogger logger = new DemoLogger(RestTemplateTimeInterceptor.class);

    /**
     * 最大响应时间
     */
    @Value("#{${httpBigTime:1000}}")
    private Long httpBigTime;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String url = request.getURI().toString();
        tranceRequest(request, body);
        //日志
        long start = System.currentTimeMillis();
        try {
            ClientHttpResponse clientHttpResponse = execution.execute(request, body);
            tranceResponse(clientHttpResponse);
            return clientHttpResponse;
        } catch (Exception e) {
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            if (time > httpBigTime) {
                //日志
                logger.warn("url执行时间比较长：{};执行时间为：{}毫秒", url, time);
            }
        }
    }

    private void tranceRequest(HttpRequest request, byte[] body) {
        logger.info("请求参数===uri:{},method:{},headers:{},body:{}", request.getURI().toString(), request.getMethod(),
                request.getHeaders(), new String(body, StandardCharsets.UTF_8));
    }

    private void tranceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder responseBody = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        if (line != null) {
            responseBody.append(line);
            responseBody.append('\n');
            line = bufferedReader.readLine();
        }
        logger.info("返回结果===status code:{},status text:{},headers:{},response body:{}", response.getStatusCode(),
                response.getStatusText(), response.getHeaders(), responseBody.toString());
    }
}
