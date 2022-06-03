package com.wj.crowd.entity.Vo.project;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wj.crowd.entity.Do.Picture;
import com.wj.crowd.entity.Do.User;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 14:55
 */
@Data
@ApiModel(value="ProjectVo对象", description="")
public class ProjectVo {

    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "项目名称")
    private String name;

    @ApiModelProperty(value = "项目标题")
    private String title;

    @ApiModelProperty(value = "项目类型")
    private String type;

    @ApiModelProperty(value = "目标金额")
    private Double targetMoney;

    @ApiModelProperty(value = "已筹金额")
    private Double supportMoney;

    @ApiModelProperty(value = "支持人数")
    private Long supporterNumber;

    @ApiModelProperty(value = "关注人数")
    private Long followerNumber;

    @ApiModelProperty(value = "评论数量")
    private Long commentNumber;


    @ApiModelProperty(value = "项目头图（1080px * 1080px）")
    private String headPicture;

    @ApiModelProperty(value = "项目首页推荐图片（1080px * 462px）")
    private String adPicture;

    @ApiModelProperty(value = "项目视频")
    private String video;

    @ApiModelProperty(value = "项目发起人")
    private UserVo sponsor;

    @ApiModelProperty(value = "项目回报")
    private List<RewardVo> rewardVos;

    @ApiModelProperty(value = "项目详情")
    private List<ProjectDetailVo> projectDetailVos;

    @ApiModelProperty(value = "众筹开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "众筹结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "项目状态")
    private String status;

    @ApiModelProperty(value = "项目团队")
    @TableField(exist = false)
    private List<UserVo> teamVo;

    @ApiModelProperty(value = "项目审核辅助资料")
    @TableField(exist = false)
    private List<PictureVo> projectSupportingVoList;
}
