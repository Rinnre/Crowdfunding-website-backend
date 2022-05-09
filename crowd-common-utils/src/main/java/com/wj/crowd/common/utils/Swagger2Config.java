package com.wj.crowd.common.utils;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置信息
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket webApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("mysqlApi")
                .apiInfo(mysqlApiInfo())
                .select()
                //只显示api路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/mysql/.*")))
                .build();

    }

    @Bean
    public Docket redisApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("redisApi")
                .apiInfo(redisApiInfo())
                .select()
                //只显示api路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/redis/.*")))
                .build();

    }

    @Bean
    public Docket MsmApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("msmApi")
                .apiInfo(adminApiInfo())
                .select()
                //只显示api路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/message/.*")))
                .build();

    }

    @Bean
    public Docket UserApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("UserApi")
                .apiInfo(adminApiInfo())
                .select()
                //只显示api路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/user/.*")))
                .build();

    }

    @Bean
    public Docket adminApiConfig() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                //只显示admin路径下的页面
                .paths(Predicates.and(PathSelectors.regex("/management/.*")))
                .build();

    }

    private ApiInfo mysqlApiInfo() {

        return new ApiInfoBuilder()
                .title("网站-MYSQLAPI文档")
                .description("本文档描述了网站微服务接口定义")
                .version("1.0")
                .contact(new Contact("W", "http://localhost", "1595519431@qq.com"))
                .build();
    }

    private ApiInfo adminApiInfo() {

        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统微服务接口定义")
                .version("1.0")
                .contact(new Contact("W", "http://localhost", "1595519431@qq.com"))
                .build();
    }

    private ApiInfo redisApiInfo() {

        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统微服务接口定义")
                .version("1.0")
                .contact(new Contact("W", "http://localhost", "1595519431@qq.com"))
                .build();
    }


}
