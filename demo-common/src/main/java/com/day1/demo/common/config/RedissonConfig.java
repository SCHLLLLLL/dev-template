package com.day1.demo.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Day1
 * @Date: 2020/9/14 10:30
 * @Description:
 */
@SuppressWarnings("SpellCheckingInspection")
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

//    @Value("${spring.redis.password}")
//    private String password;

    @Value(value = "#{${spring.redis.database}}")
    private int database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String format = "redis://%s:%s";
        String address = String.format(format, host, port);
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(address).setConnectionPoolSize(10);
//        if (!StringUtils.isBlank(password)) {
//            singleServerConfig.setPassword(password);
//        }
        singleServerConfig.setDatabase(database);
        @SuppressWarnings("UnnecessaryLocalVariable")
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
