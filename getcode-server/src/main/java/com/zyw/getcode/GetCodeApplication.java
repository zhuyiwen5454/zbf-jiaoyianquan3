package com.zyw.getcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: zhuyiwen
 * 作者: zhuyiwen
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
