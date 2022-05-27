package com.wj.crowd.dynamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 17:24
 */
@SpringBootApplication
@EnableDiscoveryClient()
@EnableFeignClients("com.wj.crowd.api")
public class DynamicApplication {
    public static void main(String[] args) {
        SpringApplication.run(DynamicApplication.class, args);
    }
}
