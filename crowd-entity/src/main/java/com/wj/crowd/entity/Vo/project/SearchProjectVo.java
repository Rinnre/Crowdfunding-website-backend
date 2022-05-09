package com.wj.crowd.entity.Vo.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 21:03
 */
@Data
public class SearchProjectVo {

    @ApiModelProperty(value = "搜索关键字")
    private String keywords;

    @ApiModelProperty(value = "项目类型")
    private String type;

    @ApiModelProperty(value = "项目状态")
    private String status;

    @ApiModelProperty(value = "排序方式(1：项目上线时间最近，2：支持金额最多，3：评论最多)")
    private String sortMethods;

}
