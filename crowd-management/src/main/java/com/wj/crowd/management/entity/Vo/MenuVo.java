package com.wj.crowd.management.entity.Vo;

import com.wj.crowd.management.entity.Do.Permission;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/4/13 - 9:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuVo {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "所属上级")
    private String pid;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型(1:菜单,2:权限)")
    private Integer type;

    @ApiModelProperty(value = "访问权限")
    private List<PermissionVo> permissionValue;

    @ApiModelProperty(value = "访问路径")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "组件图标")
    private String icon;

    @ApiModelProperty(value = "菜单子节点")
    private List<MenuVo> children;
}
