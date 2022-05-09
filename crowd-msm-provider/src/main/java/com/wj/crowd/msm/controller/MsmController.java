package com.wj.crowd.msm.controller;

import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.msm.service.api.MsmService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 19:55
 */
@RestController
@ApiOperation("短信发送模块")
@RequestMapping("/message")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @GetMapping("/send/code/remote/{phone}/{type}")
    @ApiOperation("发送验证码")
    public ResultEntity<String> sendCodeRemote(@PathVariable String phone,@PathVariable String type){
        try {
            msmService.sendCode(phone,type);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }



}
