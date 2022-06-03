package com.wj.crowd.entity.Vo.dynamic;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import com.wj.crowd.entity.Vo.user.SimpleUserVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 20:35
 */
@Data
public class DynamicVo {

    @ApiModelProperty(value = "动态id")
    private String id;

    @ApiModelProperty(value = "发布人id")
    private String uid;

    @ApiModelProperty(value = "发起人对象")
    private SimpleUserVo user;

    @ApiModelProperty(value = "动态内容")
    private String content;

    @ApiModelProperty(value = "动态图片")
    private List<PictureVo> pictureList;

    @ApiModelProperty(value = "动态发布时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
}
