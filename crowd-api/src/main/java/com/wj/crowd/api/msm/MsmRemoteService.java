package com.wj.crowd.api.msm;

import com.wj.crowd.common.result.ResultEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 21:30
 */
@Service
@FeignClient(value = "crowd-msm", path = "/message")
public interface MsmRemoteService {
    @GetMapping("/send/code/remote/{phone}/{type}")
    @ApiOperation("发送验证码")
    ResultEntity<String> sendCodeRemote(@PathVariable String phone, @PathVariable String type);
}
