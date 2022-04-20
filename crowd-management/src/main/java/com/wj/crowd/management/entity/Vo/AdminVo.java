package com.wj.crowd.management.entity.Vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wj
 * @descript 管理员vo对象
 * @date 2022/4/11 - 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "登录账号")
    private String loginAcct;


    @TableField("password")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "电子邮箱")
    private String email;
}
