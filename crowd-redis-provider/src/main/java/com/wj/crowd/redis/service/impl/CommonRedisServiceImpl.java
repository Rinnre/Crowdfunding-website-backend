package com.wj.crowd.redis.service.impl;

import com.wj.crowd.redis.service.api.CommonRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 16:34
 */
@Service
public class CommonRedisServiceImpl implements CommonRedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private ValueOperations<String, String> stringStringValueOperations;


    private synchronized void initValueOperation() {

        if (this.stringStringValueOperations == null) {

            ValueOperations<String, String> stringStringValueOperations =
                    stringRedisTemplate.opsForValue();
            this.stringStringValueOperations = stringStringValueOperations;
        }


    }

    /**
     * 向redis中存入key-value
     *
     * @param key
     * @param value
     */
    @Override
    public void setRedisKeyValueRemote(String key, String value) {
        initValueOperation();
        stringStringValueOperations.set(key, value);

    }

    /**
     * 存储key-value 带过期时间
     *
     * @param key
     * @param value
     * @param time     过期时间大小
     * @param timeUnit 时间单位
     */
    @Override
    public void setRedisKeyValueRemoteWithTimeout(String key, String value, Long time, TimeUnit timeUnit) {
        initValueOperation();
        stringStringValueOperations.set(key, value, time, timeUnit);
    }

    /**
     * 从redis中根据key取出对应的value
     *
     * @param key
     * @return
     */
    @Override
    public String getRedisStringValueRemoteByKey(String key) {
        initValueOperation();
        return stringStringValueOperations.get(key);
    }

    @Override
    public Boolean removeRedisKeyRemote(String key) {
        initValueOperation();
        return stringRedisTemplate.delete(key);
    }


}
