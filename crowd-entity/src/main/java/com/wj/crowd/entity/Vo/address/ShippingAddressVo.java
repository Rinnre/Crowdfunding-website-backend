package com.wj.crowd.entity.Vo.address;

import com.baomidou.mybatisplus.annotation.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/26 - 22:29
 */
@Data
public class ShippingAddressVo {
    @ApiModelProperty(value = "收货地址id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String uid;

    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    @ApiModelProperty(value = "收货人手机号")
    private String consigneePhone;

    @ApiModelProperty(value = "收货人电子邮箱")
    private String consigneeEmail;

    @ApiModelProperty(value = "收货人地址")
    private String consigneeAddress;
}
