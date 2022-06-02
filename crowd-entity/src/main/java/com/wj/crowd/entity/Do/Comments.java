package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author w
 * @since 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Comments对象", description="评论表")
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "所属上级编号")
    private String parentId;

    @ApiModelProperty(value = "来源类型")
    private String sourceType;

    @ApiModelProperty(value = "来源编号")
    private String sourceId;

    @ApiModelProperty(value = "回复者编号")
    private String memberId;

    @ApiModelProperty(value = "回复者昵称")
    private String nickname;

    @ApiModelProperty(value = "回复者头像")
    private String avatar;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "被回复评论编号")
    private String replyId;

    @ApiModelProperty(value = "被回复者编号")
    private String replyMemberId;

    @ApiModelProperty(value = "被回复者昵称")
    private String replyNickname;

    @ApiModelProperty(value = "评论状态 1（true）被举报， 0（false）正常")
    private Integer state;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
