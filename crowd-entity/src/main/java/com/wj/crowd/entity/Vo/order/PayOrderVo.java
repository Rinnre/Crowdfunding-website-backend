package com.wj.crowd.entity.Vo.order;

import com.wj.crowd.entity.Vo.address.ShippingAddressVo;
import com.wj.crowd.entity.Vo.project.RewardVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wj
 * @descript
 * @date 2022/5/30 - 13:30
 */
@Data
public class PayOrderVo {
    @ApiModelProperty(value = "订单id")
    private String id;

    @ApiModelProperty(value = "订单流水号")
    private String payNum;

    @ApiModelProperty(value = "订单总金额")
    private Double orderAmount;

    @ApiModelProperty(value = "收货地址信息id")
    private ShippingAddressVo address;

    @ApiModelProperty(value = "支持的回报档位id")
    private RewardVo rewardVo;

    @ApiModelProperty(value = "支持的回报挡数量")
    private Long rewardCount;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "订单状态（0：已取消，1：未支付，2：已支付，3：待发货）")
    private Integer orderStatus;
}
