package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2022-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PayOrder对象", description="")
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "订单流水号")
    private String payNum;

    @ApiModelProperty(value = "下单用户id")
    private String uid;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "订单总金额")
    private Double orderAmount;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneePhone;

    @ApiModelProperty(value = "收货人邮箱")
    private String consigneeEmail;

    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;

    @ApiModelProperty(value = "支持的回报档位id")
    private String rewardId;

    @ApiModelProperty(value = "支持的回报挡数量")
    private Long rewardCount;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "订单状态（0：已取消，1：未支付，2：已支付，3：待发货）")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除（0：未删除，1：已删除）")
    @TableLogic
    private Integer isDelete;


}
