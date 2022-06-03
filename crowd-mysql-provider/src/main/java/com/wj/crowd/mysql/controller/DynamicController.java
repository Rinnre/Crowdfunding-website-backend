package com.wj.crowd.mysql.controller;


import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.Dynamic;
import com.wj.crowd.entity.Do.Picture;
import com.wj.crowd.mysql.service.api.DynamicService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author w
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/mysql/dynamic")
public class DynamicController {

    @Autowired
    private DynamicService dynamicService;

    @PostMapping("/save/dynamic")
    @ApiOperation("发布动态")
    public ResultEntity<String> saveDynamic(@RequestBody Dynamic dynamic) {
        boolean saveResult = dynamicService.saveDynamic(dynamic,dynamic.getPictureList());
        if (!saveResult) {
            return ResultEntity.fail("发布失败");
        }
        return ResultEntity.success();
    }

    @ApiOperation("查看所有动态")
    @GetMapping("/get/all/dynamic")
    public ResultEntity<List<Dynamic>> getAllDynamic(){
        try {
            List<Dynamic> dynamicList = dynamicService.getAllDynamic();
            return ResultEntity.success(dynamicList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation("用户删除动态")
    @DeleteMapping("/remove/dynamic/{uid}/{dynamicId}")
    public ResultEntity<String> removeDynamic(@PathVariable String uid, @PathVariable String dynamicId) {
        try {
            boolean removeResult = dynamicService.removeDynamic(uid, dynamicId);
            if (!removeResult) {
                return ResultEntity.fail("发布失败");
            }
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @ApiOperation("查询用户动态")
    @GetMapping("/get/dynamic/by/{userId}")
    public ResultEntity<List<Dynamic>> getDynamicByUserId(@PathVariable String userId){
        try {
            List<Dynamic> dynamicList = dynamicService.getDynamicByUserId(userId);
            return ResultEntity.success(dynamicList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }

    }
}

