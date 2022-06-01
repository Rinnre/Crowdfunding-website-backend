package com.wj.crowd.entity.Vo.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "前台评论列表显示对象")
public class CommentFrontVo implements Serializable {
    @ApiModelProperty("评论编号")
    private String id;
    
    @ApiModelProperty("父编号")
    private String parentId;
    
    @ApiModelProperty("来源类型")
    private String sourceType;
    
    @ApiModelProperty("来源编号")
    private String sourceId;
    
    @ApiModelProperty("回复者编号")
    private String memberId;
    
    @ApiModelProperty("回复者昵称")
    private String nickname;
    
    @ApiModelProperty("回复者头像")
    private String avatar;
    
    @ApiModelProperty("评论内容")
    private String content;
    
    @ApiModelProperty("被回复评论编号")
    private String replyId;
    
    @ApiModelProperty("被回复者编号")
    private String replyMemberId;
    
    @ApiModelProperty("被回复者昵称")
    private String replyNickname;
    
    @ApiModelProperty(value = "层级")
    private Integer level;
    
    @ApiModelProperty(value = "下级")
    private List<CommentFrontVo> children;
    
    @ApiModelProperty("评论状态 1（true）被举报， 0（false）正常")
    private Integer state;
    
    @ApiModelProperty("创建时间")
    private Date gmtCreate;
}
