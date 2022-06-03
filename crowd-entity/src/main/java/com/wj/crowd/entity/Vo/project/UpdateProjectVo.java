package com.wj.crowd.entity.Vo.project;

import com.wj.crowd.entity.Vo.project.ProjectDetailVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/7 - 16:08
 */
@Data
public class UpdateProjectVo {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目标题")
    private String title;

    @ApiModelProperty(value = "项目类型")
    private String type;

    @ApiModelProperty(value = "项目头图（1080px * 1080px）")
    private String headPicture;

    @ApiModelProperty(value = "项目首页推荐图片（1080px * 462px）")
    private String adPicture;

    @ApiModelProperty(value = "项目视频")
    private String video;

    @ApiModelProperty(value = "项目详情")
    private List<ProjectDetailVo> projectDetailVos;

    @ApiModelProperty(value = "众筹开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "众筹结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "项目状态")

    private String Status;
}
