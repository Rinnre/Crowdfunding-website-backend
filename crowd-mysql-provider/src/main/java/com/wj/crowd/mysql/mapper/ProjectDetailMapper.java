package com.wj.crowd.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Do.ProjectDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 16:29
 */
public interface ProjectDetailMapper extends BaseMapper<ProjectDetail> {

    List<ProjectDetail> getProjectDetailsByProjectId(@Param("id") String projectId);
}
