package com.day1.demo.mapper.config.datasource;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author: LinHangHui
 * @Date: 2020/10/24 16:25
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ShardingMasterSlaveConfig.class, DruidDataSourceConfig.class})
public class ShardDataSourceConfig {

    @Autowired
    private ShardingMasterSlaveConfig shardingMasterSlaveConfig;

    @Autowired
    private DruidDataSourceConfig druidDataSourceConfig;

    @Bean
    public DataSource masterSlaveResource() throws SQLException {

        shardingMasterSlaveConfig.getDataSources().forEach((k,v) -> configDataSource(v));
        HashMap<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.putAll(shardingMasterSlaveConfig.getDataSources());

        DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
                shardingMasterSlaveConfig.getMasterSlaveRule(), new HashMap<>(),
                shardingMasterSlaveConfig.getProps());
        log.info("===========sharding-jdbc===>masterSlaveDataSource **** Read and write separation initialization success! *****");
        return dataSource;
    }

    private void configDataSource(DruidDataSource druidDataSource) {
        druidDataSource.setInitialSize(druidDataSourceConfig.getDruid().getInitialSize());
        druidDataSource.setMaxActive(druidDataSourceConfig.getDruid().getMaxActive());
        druidDataSource.setMinIdle(druidDataSourceConfig.getDruid().getMinIdle());
        druidDataSource.setMaxWait(druidDataSourceConfig.getDruid().getMaxWait());
        druidDataSource
                .setPoolPreparedStatements(druidDataSourceConfig.getDruid().isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(
                druidDataSourceConfig.getDruid().getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setTimeBetweenEvictionRunsMillis(
                druidDataSourceConfig.getDruid().getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(
                druidDataSourceConfig.getDruid().getMinEvictableIdleTimeMillis());
        druidDataSource.setMaxEvictableIdleTimeMillis(druidDataSourceConfig.getDruid().getMaxEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(druidDataSourceConfig.getDruid().getValidationQuery());
        druidDataSource.setTestWhileIdle(druidDataSourceConfig.getDruid().isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidDataSourceConfig.getDruid().isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidDataSourceConfig.getDruid().isTestOnReturn());

        // 支持表情
        StringTokenizer tokenizer = new StringTokenizer("SET NAMES utf8mb4", ";");
        //重点设置该参数
        druidDataSource.setConnectionInitSqls(Collections.list(tokenizer));

        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true);
        statFilter.setSlowSqlMillis(1000);
        statFilter.setMergeSql(true);
        druidDataSource.getProxyFilters().add(statFilter);
    }

}
