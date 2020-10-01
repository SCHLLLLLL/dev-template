package com.day1.demo.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;


/**
 * @author: linhanghui
 * @Date: 2020/9/14 10:31
 * @Description:
 */
@Configuration
public class RestTemplateConfig {

    private final static Integer RETRY_TIMES = 3;

    @Setter(onMethod = @__(@Autowired))
    private RestTemplateTimeInterceptor restTemplateTimeInterceptor;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter targetConverter = (StringHttpMessageConverter) converter;
                targetConverter.setDefaultCharset(StandardCharsets.UTF_8);
            } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Chongqing"));
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                MappingJackson2HttpMessageConverter targetConverter = (MappingJackson2HttpMessageConverter) converter;
                targetConverter.setObjectMapper(objectMapper);
                targetConverter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));

            }
        }
        restTemplate.setInterceptors(Collections.singletonList(restTemplateTimeInterceptor));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                // 连接上服务器(握手成功)的时间，超出该时间抛出connect
                .setConnectTimeout(5000)
                // 从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException:
                .setConnectionRequestTimeout(5000)
                .build();

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 设置整个连接池最大连接数 根据自己的场景决定
        poolingHttpClientConnectionManager.setMaxTotal(400);
        // 路由是对maxTotal的细分,每个域名200个连接
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(200);

        return HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new HttpRequestRetryHandler() {
                    @Override
                    public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
                        if ((exception != null)) {
                            // 超时重试3次
                            return executionCount < RETRY_TIMES;
                        }
                        return false;
                    }
                })
                .build();
    }
}
