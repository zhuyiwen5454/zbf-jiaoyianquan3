package com.wts.getcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: WTS
 * 作者: WTS
 * 日期: 2020/9/10 18:55
 * 描述:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GetCodeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GetCodeApplication.class);
    }
}
