package com.wj.crowd.entity.Vo.project;

import com.wj.crowd.entity.Vo.user.SimpleUserVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 20:47
 */
@Data
public class SimpleProjectVo {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "项目标题")
    private String title;

    @ApiModelProperty(value = "已筹金额")
    private Double supportMoney;

    @ApiModelProperty(value = "支持人数")
    private Long supporterNumber;

    @ApiModelProperty(value = "项目头图（1080px * 1080px）")
    private String headPicture;

    @ApiModelProperty(value = "项目进度")
    private Double percentage;

    @ApiModelProperty(value = "项目发起人")
    private SimpleUserVo simpleUserVo;
}
