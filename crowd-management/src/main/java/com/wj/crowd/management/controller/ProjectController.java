package com.wj.crowd.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Do.SimpleProject;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wj
 * @descript
 * @date 2022/6/4 - 16:32
 */
@RestController
@RequestMapping("/management/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @PostMapping("/get/all/project/pages/{page}/{size}")
    @ApiOperation("分页带条件查询所有项目")
    public ResultEntity<Page<SimpleProject>> getAllProject(@PathVariable Long page,
                                                           @PathVariable Long size,
                                                           @RequestBody(required = false) SearchProjectVo searchProjectVo) {
        if (null != searchProjectVo) {
            return mysqlRemoteService.getProjectPagesRemote(page, size, searchProjectVo);
        }
        return mysqlRemoteService.getProjectPagesRemote(page, size);
    }

    @GetMapping("/get/project/detail/{project_id}")
    @ApiOperation("根据id查询项目详细信息")
    ResultEntity<Project> getProjectByProjectId(@PathVariable("project_id") String projectId) {
        return mysqlRemoteService.getProjectByProjectIdRemote(projectId);
    }

    @ApiOperation("后台项目审核")
    @PutMapping("/modify/project/status/{projectId}/{status}")
    public ResultEntity<String> modifyProjectStatus(@PathVariable String projectId,
                                                    @PathVariable String status) {
        return mysqlRemoteService.modifyProjectStatus(projectId, status);
    }

}
