package com.zbf.user;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/7  23:46
 * 描述: 用户服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.zbf"})
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class);
    }
}
