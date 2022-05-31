package com.wj.crowd.api.redis;

import com.wj.crowd.common.result.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 17:39
 */
@Service
@FeignClient(value = "crowd-redis", path = "/redis/common")
public interface CommonRedisRemoteService {

    @PostMapping("set/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value);

    @PostMapping("/set/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") Long time,
            @RequestParam("timeUnit") TimeUnit timeUnit);

    @GetMapping("/get/redis/string/value/remote/by/key")
    public ResultEntity<String> getRedisStringValueRemoteByKey(
            @RequestParam("key") String key);

    @DeleteMapping("/remove/redis/key/remote")
    public ResultEntity<String> removeRedisKeyRemote(
            @RequestParam("key") String key);

    @PostMapping("/set/key/object/remote")
    public ResultEntity<String> setRedisKeyObjectRemote(@RequestParam("key") String key,
                                                        @RequestBody Object objects);

    @GetMapping("/get/object/remote/by/key")
    public ResultEntity<Object> getObject(@RequestParam("key") String key);

    @DeleteMapping("/remove/redis/hash/remote/by/key")
    public ResultEntity<String> removeRedisHashRemoteByKey(@RequestParam("key") String key,
                                                           @RequestParam("hashKey") String hashKey);

    @GetMapping("/get/redis/hash/remote/by/key")
    public ResultEntity<Object> getRedisHashRemoteByKey(@RequestParam("key") String key,
                                                        @RequestParam("hashKey") String hashKey);

    @PostMapping("/set/redis/key/hash/remote")
    public ResultEntity<String> setRedisKeyHashRemote(@RequestParam("key") String key,
                                                      @RequestParam("hashKey") String hashKey,
                                                      @RequestBody Object object);

    @GetMapping("/get/redis/all/hash/key/remote")
    public ResultEntity<Boolean> getRedisAllHashKeyRemote(@RequestParam("key") String key,
                                                          @RequestParam("hashKey")String hashKey);

    @GetMapping("/get/redis/all/hash/by/key/remote")
    public ResultEntity<List<Object>> getRedisAllHashByKeyRemote(@RequestParam("key") String key);
}
