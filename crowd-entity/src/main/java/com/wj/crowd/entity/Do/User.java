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
 * @since 2022-05-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="User对象", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "uid", type = IdType.ASSIGN_ID)
    private String uid;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "手机号")
    private String phone;

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

    @ApiModelProperty(value = "账号状态(0:不可用,1:可用)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除（0：未删除，1：已删除）")
    @TableLogic
    private Integer isDelete;


}
