package com.wj.crowd.mysql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 16:40
 */
@Configuration
@ComponentScan("com.wj.crowd")
public class MysqlConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
