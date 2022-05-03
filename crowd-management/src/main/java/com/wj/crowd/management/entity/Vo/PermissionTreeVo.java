package com.wj.crowd.management.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/2 - 20:47
 */
@Data
public class PermissionTreeVo {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "权限名称")
    private String label;

    private List<PermissionTreeVo> children;
}
