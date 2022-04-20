package com.wj.crowd.management.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wj
 * @descript
 * @date 2022/4/11 - 11:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchVo {
    @ApiModelProperty(value = "登录账号")
    private String loginAcct;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "电子邮箱")
    private String email;
}
