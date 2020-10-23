package com.day1.demo.mapper.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author: linhanghui
 * @Date: 2020/9/14 20:17
 * @Description:
 */
@Configuration
public class DruidDataSourceConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    private DataSource druid() {
        return new DruidDataSource();
    }
}
