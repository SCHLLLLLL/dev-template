package com.day1.demo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: LinHangHui
 * @Date: 2020/9/16 15:28
 * @Description:
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.day1.demo.*"})
public class DemoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApiApplication.class, args);
    }
}
