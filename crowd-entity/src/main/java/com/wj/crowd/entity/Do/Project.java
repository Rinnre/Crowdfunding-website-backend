package com.wj.crowd.entity.Do;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

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
 * @since 2022-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Project对象", description="")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
    private User sponsor;

    @ApiModelProperty(value = "项目回报")
    @TableField(exist = false)
    private List<Reward> rewards;

    @ApiModelProperty(value = "项目详情")
    @TableField(exist = false)
    private List<ProjectDetail> projectDetails;

    @ApiModelProperty(value = "众筹开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "众筹结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "项目状态")
    private String status;

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
