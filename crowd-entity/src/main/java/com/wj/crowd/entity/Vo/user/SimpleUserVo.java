package com.wj.crowd.entity.Vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 20:47
 */
@Data
public class SimpleUserVo {
    @ApiModelProperty(value = "主键id")
    private String uid;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "认证状态（0：未认证，1：已认证）")
    private Integer authStatus;

    @ApiModelProperty("登录后的token令牌")
    private String token;
}
