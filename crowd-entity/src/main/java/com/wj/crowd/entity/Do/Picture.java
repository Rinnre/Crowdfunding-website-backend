package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 20:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="Picture对象", description="")
public class Picture {

    @ApiModelProperty(value = "图片id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "图片来源类型")
    private String type;

    @ApiModelProperty(value = "图片路径")
    private  String picturePath;

    @ApiModelProperty(value = "动态id或者回报id")
    private String foreignId;

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
