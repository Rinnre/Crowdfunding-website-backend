package com.wj.crowd.redis.service.impl;

import com.wj.crowd.redis.service.api.CommonRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private RedisTemplate redisTemplate;

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

    @Override
    public void setRedisKeyObjectRemote(String key, Object objects) {
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        stringObjectValueOperations.set(key, objects);
    }

    @Override
    public Object getObject(String key) {
        ValueOperations<String, Object> stringObjectValueOperations = redisTemplate.opsForValue();
        return stringObjectValueOperations.get(key);
    }

    @Override
    public void setRedisKeyHashRemote(String key, String hashKey, Object object) {
        HashOperations<String, String, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        stringObjectObjectHashOperations.put(key, hashKey, object);
    }

    @Override
    public Object getRedisHashRemoteByKey(String key, String hashKey) {
        HashOperations<String, String, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        return stringObjectObjectHashOperations.get(key, hashKey);
    }

    @Override
    public void removeRedisHashRemoteByKey(String key, String hashKey) {
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        stringObjectObjectHashOperations.delete(key, hashKey);
    }

    @Override
    public Boolean getRedisAllHashKeyRemote(String key, String hashKey) {
        HashOperations<String, String, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        return stringObjectObjectHashOperations.hasKey(key, hashKey);
    }

    @Override
    public List<Object> getRedisAllHashByKeyRemote(String key) {
        HashOperations<String, String, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        Map<String, Object> entries = stringObjectObjectHashOperations.entries(key);
        List<Object> objectList = new ArrayList<>();
        for(Map.Entry<String,Object> entry:entries.entrySet() ){
            objectList.add(entry.getValue());
        }
        return objectList;
    }


}
