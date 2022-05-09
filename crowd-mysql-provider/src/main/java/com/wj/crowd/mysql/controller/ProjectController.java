package com.wj.crowd.mysql.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Do.SimpleProject;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import com.wj.crowd.entity.Vo.user.UpdateProjectVo;
import com.wj.crowd.mysql.service.api.ProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author w
 * @since 2022-05-05
 */
@RestController
@RequestMapping("/mysql/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/save/project/remote")
    @ApiOperation("新建项目")
    public ResultEntity<String> saveProjectRemote(@RequestBody Project project){
        try {
            projectService.saveProject(project);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/get/project/pages/remote/{page}/{size}")
    @ApiOperation("分页带条件查询所有项目")
    public ResultEntity<Page<SimpleProject>> getProjectPagesRemote(@PathVariable Long page,
                                                                   @PathVariable Long size,
                                                                   @RequestBody(required = false) SearchProjectVo searchProjectVo){
        try {
            Page<SimpleProject> projectPages =projectService.getProjectPages(page,size,searchProjectVo);
            return ResultEntity.success(projectPages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }

    }

    @GetMapping("/get/project/detail/remote/{project_id}")
    @ApiOperation("根据id查询项目详细信息")
    public ResultEntity<Project> getProjectByProjectIdRemote(@PathVariable("project_id") String projectId){
        try {
            Project project = projectService.getProjectByProjectId(projectId);
            return ResultEntity.success(project);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

   @PutMapping("/modify/project/remote")
   @ApiOperation("根据id更新项目详细信息")
    public ResultEntity<String> modifyProjectRemote(@RequestBody UpdateProjectVo updateProjectVo){
       try {
           projectService.modifyProjectRemote(updateProjectVo);
           return ResultEntity.success();
       } catch (Exception e) {
           e.printStackTrace();
           return ResultEntity.fail(e.getMessage());
       }
   }


}

