package com.wj.crowd.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wj
 * @descript
 * @date 2022/5/31 - 14:29
 */
@SpringBootApplication
@EnableDiscoveryClient()
@EnableFeignClients("com.wj.crowd.api")
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }
}
