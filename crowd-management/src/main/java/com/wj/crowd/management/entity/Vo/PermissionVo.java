package com.wj.crowd.management.entity.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/4/20 - 21:47
 */
@Data
public class PermissionVo {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "权限值")
    private String value;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;
}
