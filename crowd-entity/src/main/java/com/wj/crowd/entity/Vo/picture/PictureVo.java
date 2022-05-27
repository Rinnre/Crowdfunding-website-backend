package com.wj.crowd.entity.Vo.picture;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 20:45
 */
@Data
public class PictureVo {
    @ApiModelProperty(value = "图片id")
    private String id;

    @ApiModelProperty(value = "图片路径")
    private  String picturePath;
}
