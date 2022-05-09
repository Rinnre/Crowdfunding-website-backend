package com.wj.crowd.mysql.mapper;

import com.wj.crowd.entity.Do.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.crowd.entity.Do.SimpleProject;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author w
 * @since 2022-05-05
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<SimpleProject> getProjectPages(@Param("page") Long page, @Param("size") Long size, @Param("searchProjectVo") SearchProjectVo searchProjectVo);

    Long getProjectPagesCount( @Param("searchProjectVo") SearchProjectVo searchProjectVo);

    Project getProjectByProjectId(String projectId);
}
