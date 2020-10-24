package com.day1.demo.mapper.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: LinHangHui
 * @Date: 2020/10/24 16:21
 */
@Data
@ConfigurationProperties(prefix = "sharding.jdbc")
public class ShardingMasterSlaveConfig {

    private Map<String, DruidDataSource> dataSources = new HashMap<>();
    private MasterSlaveRuleConfiguration masterSlaveRule;
    private Properties props;
}
