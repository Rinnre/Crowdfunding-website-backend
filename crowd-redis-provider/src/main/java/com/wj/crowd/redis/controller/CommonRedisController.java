package com.wj.crowd.redis.controller;

import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.redis.service.api.CommonRedisService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 16:33
 */
@RestController
@RequestMapping("/redis/common")
@Api("公共redis操作")
public class CommonRedisController {

    @Autowired
    private CommonRedisService commonRedisService;

    @PostMapping("set/key/value/remote")
    public ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value) {

        try {
            commonRedisService.setRedisKeyValueRemote(key, value);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }

    }

    @PostMapping("/set/key/object/remote")
    public ResultEntity<String> setRedisKeyObjectRemote(@RequestParam("key") String key,
                                                        @RequestBody Object objects) {
        try {
            commonRedisService.setRedisKeyObjectRemote(key, objects);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @GetMapping("/get/object/remote/by/key")
    public ResultEntity<Object> getObject(@RequestParam("key") String key) {
        try {
            Object object = commonRedisService.getObject(key);
            return ResultEntity.success(object);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/set/key/value/remote/with/timeout")
    public ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") Long time,
            @RequestParam("timeUnit") TimeUnit timeUnit) {
        try {
            commonRedisService.setRedisKeyValueRemoteWithTimeout(key, value, time, timeUnit);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @GetMapping("/get/redis/string/value/remote/by/key")
    public ResultEntity<String> getRedisStringValueRemoteByKey(
            @RequestParam("key") String key) {
        try {
            String result = commonRedisService.getRedisStringValueRemoteByKey(key);
            return ResultEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }


    @DeleteMapping("/remove/redis/key/remote")
    public ResultEntity<String> removeRedisKeyRemote(
            @RequestParam("key") String key) {

        try {
            Boolean result = commonRedisService.removeRedisKeyRemote(key);
            if (!result) {
                return ResultEntity.fail(ResultCodeEnum.DELETE_DATA_ERROR.getMessage());
            }
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/set/redis/key/hash/remote")
    public ResultEntity<String> setRedisKeyHashRemote(@RequestParam("key") String key,
                                                      @RequestParam("hashKey") String hashKey,
                                                      @RequestBody Object object) {
        try {
            commonRedisService.setRedisKeyHashRemote(key, hashKey, object);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @GetMapping("/get/redis/hash/remote/by/key")
    public ResultEntity<Object> getRedisHashRemoteByKey(@RequestParam("key") String key,
                                                        @RequestParam("hashKey") String hashKey) {
        try {
            Object object = commonRedisService.getRedisHashRemoteByKey(key, hashKey);
            return ResultEntity.success(object);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @DeleteMapping("/remove/redis/hash/remote/by/key")
    public ResultEntity<String> removeRedisHashRemoteByKey(@RequestParam("key") String key,
                                                           @RequestParam("hashKey") String hashKey) {
        try {
            commonRedisService.removeRedisHashRemoteByKey(key, hashKey);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @GetMapping("/get/redis/all/hash/key/remote")
    public ResultEntity<Boolean> getRedisAllHashKeyRemote(@RequestParam("key") String key,
                                                               @RequestParam("hashKey")String hashKey){
        try {
            Boolean result = commonRedisService.getRedisAllHashKeyRemote(key,hashKey);
            return ResultEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @GetMapping("/get/redis/all/hash/by/key/remote")
    public ResultEntity<List<Object>> getRedisAllHashByKeyRemote(@RequestParam("key") String key){
        try {
            List<Object> result = commonRedisService.getRedisAllHashByKeyRemote(key);
            return ResultEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }
}
