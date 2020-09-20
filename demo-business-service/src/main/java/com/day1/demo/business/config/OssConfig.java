package com.day1.demo.business.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: LinHangHui
 * @Date: 2020/9/16 16:44
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "component.oss")
public class OssConfig {

    private String endpoint;
    private String key;
    private String secret;
    private String address;
    private String bucket;
    private String videoBucket;
    private String videoDir;
    private String videoAddress;
}
