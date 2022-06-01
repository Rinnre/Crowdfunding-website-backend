package com.wj.crowd.entity.Vo.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(description = "评论信息对象")
public class CommentFormVo implements Serializable {
    @ApiModelProperty("父评论编号")
    @NotBlank(message = "父评论编号不能为空")
    private String parentId;

    @ApiModelProperty("来源类型")
    @NotBlank(message = "来源类型不能为空")
    private String sourceType;

    @ApiModelProperty(value = "回复者昵称")
    private String nickname;

    @ApiModelProperty(value = "回复者编号")
    private String memberId;

    @ApiModelProperty(value = "回复者头像")
    private String avatar;

    @ApiModelProperty("来源编号")
    @NotBlank(message = "来源编号不能为空")
    private String sourceId;


    @ApiModelProperty("评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;

    @ApiModelProperty("被回复评论人编号")
    @NotBlank(message = "被回复评论人编号不能为空")
    private String replyId;

    @ApiModelProperty(value = "被回复者昵称")
    private String replyNickname;
}
