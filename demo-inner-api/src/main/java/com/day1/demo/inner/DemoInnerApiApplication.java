package com.day1.demo.inner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: LinHangHui
 * @Date: 2020/10/22 13:41
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.day1.demo"})
public class DemoInnerApiApplication {

    public static void main(String[] args) {
            SpringApplication.run(DemoInnerApiApplication.class, args);
        }
}
