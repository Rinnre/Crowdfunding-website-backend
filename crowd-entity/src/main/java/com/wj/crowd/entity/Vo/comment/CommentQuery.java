package com.wj.crowd.entity.Vo.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "评论列表查询对象")
public class CommentQuery implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("评论来源")
    private String source;
    
    @ApiModelProperty("评论内容")
    private String content;
    
    @ApiModelProperty("评论类型 report 被举报， normal 正常")
    private String state;
    
    @ApiModelProperty(value = "查询开始时间", example = "2019-01-01 10:10:10")
    private String begin;
    
    @ApiModelProperty(value = "查询结束时间", example = "2019-12-01 10:10:10")
    private String end;
}
