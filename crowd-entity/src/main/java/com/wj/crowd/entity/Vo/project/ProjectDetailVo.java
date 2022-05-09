package com.wj.crowd.entity.Vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 15:08
 */
@Data
public class ProjectDetailVo {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属项目id")
    private String projectId;

    @ApiModelProperty(value = "项目详情图片")
    private String picturePath;

    @ApiModelProperty(value = "项目详情类别（概述、产品说明...）")
    private String type;
}
