package com.wj.crowd.entity.Vo.project;

import com.wj.crowd.entity.Do.Picture;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 15:08
 */
@Data
public class RewardVo {
    @ApiModelProperty(value = "主键id")
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
    private List<PictureVo> pictureVos;

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
}
