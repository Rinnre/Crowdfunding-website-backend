package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

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
 * @since 2022-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Dynamic对象", description="")
public class Dynamic implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "动态id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "发布人id")
    private String uid;

    @ApiModelProperty(value = "发起人信息")
    @TableField(exist = false)
    private User user;

    @ApiModelProperty(value = "动态内容")
    private String content;

    @ApiModelProperty(value = "动态图片")
    @TableField(exist = false)
    private List<Picture> pictureList;

    @ApiModelProperty(value = "动态发布时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "动态更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除（0：未删除，1：已删除）")
    @TableLogic
    private Integer isDelete;


}
