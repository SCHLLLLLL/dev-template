package com.day1.demo.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: LinHangHui
 * @Date: 2020/11/25 21:11
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.day1.demo.*"})
public class XxlJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxlJobApplication.class, args);
    }
}
