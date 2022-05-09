package com.wj.crowd.redis.service.api;

import java.util.concurrent.TimeUnit;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 16:33
 */
public interface CommonRedisService {
    void setRedisKeyValueRemote(String key, String value);

    void setRedisKeyValueRemoteWithTimeout(String key, String value, Long time, TimeUnit timeUnit);

    String getRedisStringValueRemoteByKey(String key);

    Boolean removeRedisKeyRemote(String key);
}
