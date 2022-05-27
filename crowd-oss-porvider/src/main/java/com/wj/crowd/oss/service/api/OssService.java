package com.wj.crowd.oss.service.api;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 16:32
 */
public interface OssService {
    String fileUpload(MultipartFile file);

}
