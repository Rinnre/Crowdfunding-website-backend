package com.wj.crowd.management.entity.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wj
 * @descript
 * @date 2022/4/12 - 15:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleVo {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;
}
