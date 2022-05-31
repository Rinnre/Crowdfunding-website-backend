package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author w
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProjectDetail对象", description="")
public class ProjectDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属项目id")
    private String projectId;

    @ApiModelProperty(value = "项目详情图片")
    private String picturePath;

    @ApiModelProperty(value = "项目详情类别（概述、产品说明...）")
    private String type;

    @ApiModelProperty(value = "图片顺序")
    private Integer projectOrder;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除（0：未删除，1：已删除）")
    @TableLogic
    private Integer isDelete;

}
