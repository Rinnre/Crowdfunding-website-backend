package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

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
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Reward对象", description="")
public class Reward implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属项目id")
    private String projectId;


    @ApiModelProperty(value = "回报标题")
    private String title;

    @ApiModelProperty(value = "支持金额")
    private Double supportMoney;

    @ApiModelProperty(value = "回报描述")
    private String description;

    @ApiModelProperty(value = "回报图片")
    @TableField(exist = false)
    private List<Picture> picture;

    @ApiModelProperty(value = "是否包邮（0：部分地区不包邮，1：包邮）")
    private Integer postage;

    @ApiModelProperty(value = "发货日期（1：一个月以内，2：俩个月以内，3：三个月以内）")
    private Integer shipmentsDay;

    @ApiModelProperty(value = "发货方式（1：实物邮寄，2：虚拟回报，3：虚拟回报+实物邮寄）")
    private Integer shippingMethod;

    @ApiModelProperty(value = "是否限量（-1：不限量，限量 0：售空，>1:有货）")
    private Integer limitNumber;

    @ApiModelProperty(value = " 库存个数（-1：不限量）")
    private Integer inventoryNumber;

    @ApiModelProperty(value = "是否限购（0：不限购，>1:限购n个）")
    private Integer limitBuy;

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
