package com.wj.crowd.msm.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 19:51
 */
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsmProperties {

    private String regionId;

    private String accessKeyId;

    private String secret;


}
