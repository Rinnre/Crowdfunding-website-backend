package com.wj.crowd.api.oss;

import com.wj.crowd.common.result.ResultEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 17:37
 */
@Service
@FeignClient(value = "crowd-oss", path = "/oss")
public interface OssRemoteService {

    @PostMapping(value = "fileUpload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("文件上传")
    public ResultEntity<String> fileUpload(MultipartFile file);
}
