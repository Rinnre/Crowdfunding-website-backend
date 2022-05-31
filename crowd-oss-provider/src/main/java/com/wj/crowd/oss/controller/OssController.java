package com.wj.crowd.oss.controller;

import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.oss.service.api.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 16:31
 */
@RestController
@RequestMapping("oss")
@Api("文件上传服务")
public class OssController {
    @Autowired
    private OssService ossService;


    @PostMapping("fileUpload")
    @ApiOperation("文件上传")
    public ResultEntity<String> fileUpload(MultipartFile file){
        try {
            String url =ossService.fileUpload(file);
            return ResultEntity.success(url);
        } catch (Exception e) {
            return ResultEntity.fail(e.getMessage());
        }
    }
}
