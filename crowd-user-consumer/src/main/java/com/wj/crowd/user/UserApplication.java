package com.wj.crowd.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 16:30
 */
@SpringBootApplication
@EnableDiscoveryClient()
@EnableFeignClients("com.wj.crowd.api")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
