package com.wj.crowd.oss.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 16:30
 */
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OssProperties {
    private String endpoint;

    private String accessKeyId;

    private String secret;

    private String bucket;
}
