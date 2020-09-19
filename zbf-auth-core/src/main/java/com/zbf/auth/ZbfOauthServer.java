package com.zbf.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: zhuyiwen
 * 作者: zhuyiwen
 * 日期: 2020/9/6  14:11
 * 描述:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ZbfOauthServer {
    public static void main(String[] args) {
        SpringApplication.run(ZbfOauthServer.class);
    }
}
