package com.wj.crowd.entity.Vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 20:49
 */
@Data
public class UserAuthInfoVo {
    @ApiModelProperty(value = "主键id")
    private String uid;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "认证类型（1：身份证，2：驾驶证，3：户口本）")
    private String authType;

    @ApiModelProperty(value = "证件号码")
    private String idNumber;

    @ApiModelProperty(value = "正面证件照")
    private String frontIdPicture;

    @ApiModelProperty(value = "反面证件照")
    private String backIdPicture;

    @ApiModelProperty(value = "认证状态")
    private Integer authStatus;
}
