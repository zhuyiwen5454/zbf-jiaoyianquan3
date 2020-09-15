package gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: LCG
 * 作者: LCG
 * 日期: 2020/9/6  13:04
 * 描述:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ZbfGateWatApp {

    public static void main(String[] args) {
        SpringApplication.run(ZbfGateWatApp.class);
    }

}
