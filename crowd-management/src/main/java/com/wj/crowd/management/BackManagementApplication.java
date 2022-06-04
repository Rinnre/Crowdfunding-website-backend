package com.wj.crowd.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wj
 * @descript 后台管理系统启动类
 * @date 2022/4/10 - 21:43
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableDiscoveryClient
@EnableFeignClients("com.wj.crowd.api")
public class BackManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackManagementApplication.class,args);
    }
}
