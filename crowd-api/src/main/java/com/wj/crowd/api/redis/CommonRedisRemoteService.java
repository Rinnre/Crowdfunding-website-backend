package com.wj.crowd.api.redis;

import com.wj.crowd.common.result.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
