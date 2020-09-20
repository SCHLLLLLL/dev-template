package com.day1.demo.mapper.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 20:17
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidDataSourceConfig {

    private DruidDataSource druid;
}
