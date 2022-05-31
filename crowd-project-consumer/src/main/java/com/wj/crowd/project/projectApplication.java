package com.wj.crowd.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wj
 * @descript
 * @date 2022/5/27 - 20:42
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.wj.crowd.api")
public class projectApplication {
    public static void main(String[] args) {
        SpringApplication.run(projectApplication.class,args);
    }
}
