package com.wj.crowd.redis.service.api;

import java.util.List;
import java.util.Objects;
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

    void setRedisKeyObjectRemote(String key, Object objects);

    Object getObject(String key);

    void setRedisKeyHashRemote(String key, String hashKey, Object object);

    Object getRedisHashRemoteByKey(String key, String hashKey);

    void removeRedisHashRemoteByKey(String key, String hashKey);

    Boolean getRedisAllHashKeyRemote(String key,String hashKey);

    List<Object> getRedisAllHashByKeyRemote(String key);
}
