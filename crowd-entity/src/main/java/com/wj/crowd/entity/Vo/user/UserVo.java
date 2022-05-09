package com.wj.crowd.entity.Vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 20:47
 */
@Data
public class UserVo {
    @ApiModelProperty(value = "主键id")
    private String uid;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "性别（2：保密，０：女，１：男）")
    private Integer gender;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "个人简介")
    private String biography;

    @ApiModelProperty(value = "认证状态（0：未认证，1：认证中，2：已认证）")
    private Integer authStatus;
}
