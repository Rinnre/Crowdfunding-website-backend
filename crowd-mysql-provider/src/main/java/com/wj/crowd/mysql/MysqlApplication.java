package com.wj.crowd.mysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author wj
 * @descript
 * @date 2022/5/4 - 16:41
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MysqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlApplication.class,args);
    }
}
