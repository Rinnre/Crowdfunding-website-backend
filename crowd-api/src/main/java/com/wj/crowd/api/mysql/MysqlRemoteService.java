package com.wj.crowd.api.mysql;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Do.SimpleProject;
import com.wj.crowd.entity.Do.User;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import com.wj.crowd.entity.Vo.user.UpdateProjectVo;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @author wj
 * @descript
 * @date 2022/5/9 - 14:42
 */
@Service
@FeignClient("crowd-mysql")
public interface MysqlRemoteService {
    @GetMapping("/mysql/user/get/user_info/remote/{phone}")
    @ApiOperation("用户手机登录接口")
    ResultEntity<User> getUserByPhoneRemote(@PathVariable String phone);

    @PostMapping("/mysql/user/save/user/remote")
    @ApiOperation("用户注册")
    ResultEntity<String> saveUserRemote(@RequestBody User user);

    @GetMapping("/mysql/user/get/user_info_detail/remote/{uid}")
    @ApiOperation("查询用户详细信息")
    ResultEntity<User> getUserByUidRemote(@PathVariable String uid);

    @PutMapping("/mysql/user/modify/user_info/remote")
    @ApiOperation("更新用户信息")
    ResultEntity<String> modifyUserRemote(@RequestBody User user);

    @PutMapping("/mysql/user/modify/auth_info/remote")
    @ApiOperation("更新认证信息")
    ResultEntity<String> modifyAuthInfoRemote(UserAuthInfoVo userAuthInfoVo);

    @PostMapping("/mysql/project/save/project/remote")
    @ApiOperation("新建项目")
    public ResultEntity<String> saveProjectRemote(@RequestBody Project project);

    @PostMapping("/mysql/project/get/project/pages/remote/{page}/{size}")
    @ApiOperation("分页带条件查询所有项目")
    public ResultEntity<Page<SimpleProject>> getProjectPagesRemote(@PathVariable Long page,
                                                                   @PathVariable Long size,
                                                                   @RequestBody(required = false) SearchProjectVo searchProjectVo);
    @GetMapping("/mysql/project/get/project/detail/remote/{project_id}")
    @ApiOperation("根据id查询项目详细信息")
    public ResultEntity<Project> getProjectByProjectIdRemote(@PathVariable("project_id") String projectId);

    @PutMapping("/mysql/project/modify/project/remote")
    @ApiOperation("根据id更新项目详细信息")
    public ResultEntity<String> modifyProjectRemote(@RequestBody UpdateProjectVo updateProjectVo);
}
