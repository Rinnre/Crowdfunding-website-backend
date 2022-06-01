package com.wj.crowd.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.api.redis.CommonRedisRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.common.utils.JwtHelper;
import com.wj.crowd.entity.Do.*;
import com.wj.crowd.entity.Vo.comment.CommentFormVo;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import com.wj.crowd.entity.Vo.project.*;
import com.wj.crowd.entity.Vo.user.SimpleUserVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @descript
 * @date 2022/5/27 - 15:42
 */
@RestController
@Api("项目模块")
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @Autowired
    private CommonRedisRemoteService commonRedisRemoteService;

    @GetMapping("/get/project/info/{project_name}")
    @ApiOperation("获取项目信息")
    public ResultEntity<ProjectVo> getProjectInfo(HttpServletRequest request,
                                                  @PathVariable("project_name") String projectName) {
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        ResultEntity<Object> redisHashRemoteByKey = commonRedisRemoteService.getRedisHashRemoteByKey(userId + CrowdConstant.TEMPORARY_PROJECT, projectName);
        if (!redisHashRemoteByKey.isSuccess()) {
            return ResultEntity.fail();
        }
        Object data = redisHashRemoteByKey.getData();
        if (null == data) {
            return ResultEntity.fail();
        }

        Project project = JSON.parseObject(JSON.toJSONString(data), new TypeReference<Project>() {
        });

        ProjectVo projectVo = projectToProjectVo(project);

        return ResultEntity.success(projectVo);

    }

    @PostMapping("/save/base/project/info")
    @ApiOperation("项目基本信息保存")
    public ResultEntity<String> saveBaseProjectInfo(HttpServletRequest request, @RequestBody ProjectVo projectVo) {

        if (null == projectVo) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 开启一个项目的创建
        Project project = new Project();
        // 属性复制
        BeanUtils.copyProperties(projectVo, project);

        // 调用redis服务将项目存入缓存中
        // 自动生成项目key
        ResultEntity<Boolean> redisAllHashKeyRemote = commonRedisRemoteService.getRedisAllHashKeyRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        // 判断返回结果
        if (!redisAllHashKeyRemote.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        Boolean result = redisAllHashKeyRemote.getData();
        if (result) {
            return ResultEntity.fail("项目标题已被使用！");
        }

        ResultEntity<String> stringResultEntity = commonRedisRemoteService.setRedisKeyHashRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName(), project);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }

    @PostMapping("/save/project/money")
    @ApiOperation("项目基本信息保存")
    public ResultEntity<String> saveProjectMoney(HttpServletRequest request, @RequestBody ProjectVo projectVo) {

        if (null == projectVo) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 调用redis服务将项目存入缓存中
        // 自动生成项目key
        ResultEntity<Boolean> redisAllHashKeyRemote = commonRedisRemoteService.getRedisAllHashKeyRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        // 判断返回结果
        if (!redisAllHashKeyRemote.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        Boolean result = redisAllHashKeyRemote.getData();
        if (!result) {
            return ResultEntity.fail("异常数据");
        }

        ResultEntity<Object> redisHashRemoteByKey = commonRedisRemoteService.getRedisHashRemoteByKey(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        if (!redisHashRemoteByKey.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }


        Project project = JSON.parseObject(JSON.toJSONString(redisHashRemoteByKey.getData()), new TypeReference<Project>() {
        });

        project.setTargetMoney(projectVo.getTargetMoney());

        ResultEntity<String> stringResultEntity = commonRedisRemoteService.setRedisKeyHashRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName(), project);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }

    @PostMapping("/save/project/reward")
    @ApiOperation("项目回报保存")
    public ResultEntity<String> saveProjectReward(HttpServletRequest request, @RequestBody ProjectVo projectVo) {

        if (null == projectVo) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 调用redis服务将项目存入缓存中
        // 自动生成项目key
        ResultEntity<Boolean> redisAllHashKeyRemote = commonRedisRemoteService.getRedisAllHashKeyRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        // 判断返回结果
        if (!redisAllHashKeyRemote.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        Boolean result = redisAllHashKeyRemote.getData();
        if (!result) {
            return ResultEntity.fail("异常数据");
        }

        ResultEntity<Object> redisHashRemoteByKey = commonRedisRemoteService.getRedisHashRemoteByKey(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        if (!redisHashRemoteByKey.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }


        Project project = JSON.parseObject(JSON.toJSONString(redisHashRemoteByKey.getData()), new TypeReference<Project>() {
        });

        List<RewardVo> rewardVos = projectVo.getRewardVos();
        List<Reward> rewards = new ArrayList<>();
        rewardVos.forEach(rewardVo -> {
            Reward reward = new Reward();
            BeanUtils.copyProperties(rewardVo, reward);
            reward.setInventoryNumber(rewardVo.getLimitNumber());
            List<Picture> pictures = new ArrayList<>();
            List<PictureVo> pictureVos = rewardVo.getPictureVos();
            if (null != pictureVos && pictureVos.size() != 0) {
                pictureVos.forEach(pictureVo -> {
                    Picture picture = new Picture();
                    BeanUtils.copyProperties(pictureVo, picture);
                    picture.setType(CrowdConstant.PICTURE_TYPE_REWARD);
                    pictures.add(picture);
                });
                reward.setPicture(pictures);
            }
            rewards.add(reward);
        });
        project.setRewards(rewards);

        ResultEntity<String> stringResultEntity = commonRedisRemoteService.setRedisKeyHashRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName(), project);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }

    @PostMapping("/save/project/detail")
    @ApiOperation("项目详情保存")
    public ResultEntity<String> saveProjectDetail(HttpServletRequest request, @RequestBody ProjectVo projectVo) {
        if (null == projectVo) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 调用redis服务将项目存入缓存中
        // 自动生成项目key
        ResultEntity<Boolean> redisAllHashKeyRemote = commonRedisRemoteService.getRedisAllHashKeyRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        // 判断返回结果
        if (!redisAllHashKeyRemote.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        Boolean result = redisAllHashKeyRemote.getData();
        if (!result) {
            return ResultEntity.fail("异常数据");
        }

        ResultEntity<Object> redisHashRemoteByKey = commonRedisRemoteService.getRedisHashRemoteByKey(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        if (!redisHashRemoteByKey.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        Project project = JSON.parseObject(JSON.toJSONString(redisHashRemoteByKey.getData()), new TypeReference<Project>() {
        });

        List<ProjectDetailVo> projectDetailVos = projectVo.getProjectDetailVos();

        if (null == projectDetailVos || projectDetailVos.size() == 0) {
            return ResultEntity.fail("项目详情不能为空！");
        }
        List<ProjectDetail> projectDetails = new ArrayList<>();


        for (int i = 0; i < projectDetailVos.size(); i++) {
            ProjectDetail projectDetail = new ProjectDetail();
            BeanUtils.copyProperties(projectDetailVos.get(i), projectDetail);
            projectDetail.setProjectOrder(i + 1);
            projectDetails.add(projectDetail);
        }


        project.setProjectDetails(projectDetails);

        ResultEntity<String> stringResultEntity = commonRedisRemoteService.setRedisKeyHashRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName(), project);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }

    @PostMapping("/save/project/supporting")
    @ApiOperation("项目辅助资料保存 并将项目存入数据库")
    public ResultEntity<String> saveProjectSupporting(HttpServletRequest request, @RequestBody ProjectVo projectVo) {
        if (null == projectVo) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 调用redis服务将项目存入缓存中
        // 自动生成项目key
        ResultEntity<Boolean> redisAllHashKeyRemote = commonRedisRemoteService.getRedisAllHashKeyRemote(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        // 判断返回结果
        if (!redisAllHashKeyRemote.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        Boolean result = redisAllHashKeyRemote.getData();
        if (!result) {
            return ResultEntity.fail("异常数据");
        }

        ResultEntity<Object> redisHashRemoteByKey = commonRedisRemoteService.getRedisHashRemoteByKey(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());
        if (!redisHashRemoteByKey.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        Project project = JSON.parseObject(JSON.toJSONString(redisHashRemoteByKey.getData()), new TypeReference<Project>() {
        });

        List<PictureVo> projectSupportingVoList = projectVo.getProjectSupportingVoList();

        if (projectSupportingVoList != null && projectSupportingVoList.size() != 0) {
            List<Picture> projectSupportingList = new ArrayList<>();

            projectSupportingVoList.forEach(projectSupportingVo -> {
                Picture projectSupporting = new Picture();
                BeanUtils.copyProperties(projectSupportingVo, projectSupporting);
                projectSupporting.setType(CrowdConstant.PICTURE_TYPE_SUPPORTING);

                projectSupportingList.add(projectSupporting);
            });

            project.setProjectSupportingList(projectSupportingList);
        }

        // 保存项目发起人
        project.setSponsor(userId);

        // 设置项目状态为待审核
        project.setStatus("0");

        ResultEntity<String> stringResultEntity = mysqlRemoteService.saveProjectRemote(project);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(stringResultEntity.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }

        // 清除redis中的待完成项目
        ResultEntity<String> removeRedisHashRemoteByKey = commonRedisRemoteService.removeRedisHashRemoteByKey(userId + CrowdConstant.TEMPORARY_PROJECT, projectVo.getName());

        if (!removeRedisHashRemoteByKey.isSuccess()) {
            return ResultEntity.build(redisAllHashKeyRemote.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }


        return stringResultEntity;
    }


    @PostMapping("/get/simple/project/info/list/{page}/{size}")
    @ApiOperation("获取缩略展示项目列表")
    public ResultEntity<Page<SimpleProjectVo>> getSimpleProjectInfoList(@PathVariable Long page,
                                                                        @PathVariable Long size,
                                                                        @RequestBody(required = false) SearchProjectVo searchProjectVo) {
        ResultEntity<Page<SimpleProject>> projectPagesRemote;
        if (null == searchProjectVo) {
            projectPagesRemote = mysqlRemoteService.getProjectPagesRemote(page, size);
        } else {
            String status = searchProjectVo.getStatus();
            String type = searchProjectVo.getType();
            String sortMethods = searchProjectVo.getSortMethods();

            if(CrowdConstant.PROJECT_TYPE_ALL.equals(type)){
                searchProjectVo.setType(null);
            }

            if(CrowdConstant.PROJECT_SORT_METHOD_TIME.equals(sortMethods)){
                searchProjectVo.setSortMethods(CrowdConstant.PROJECT_ORDER_BY_TIME);
            }else if(CrowdConstant.PROJECT_SORT_METHOD_MONEY.equals(sortMethods)){
                searchProjectVo.setSortMethods(CrowdConstant.PROJECT_ORDER_BY_MONEY);
            }else if(CrowdConstant.PROJECT_SORT_METHOD_COMMENT.equals(CrowdConstant.PROJECT_SORT_METHOD_COMMENT)){
                searchProjectVo.setSortMethods(CrowdConstant.PROJECT_ORDER_BY_COMMENT);
            }else{
                searchProjectVo.setSortMethods(null);
            }

            if(CrowdConstant.PROJECT_STATUS_SUCCESS.equals(status)){
                searchProjectVo.setStatus(CrowdConstant.PROJECT_STATUS_SUCCESS_NUMBER);
            }else if(CrowdConstant.PROJECT_STATUS_ING.equals(status)){
                searchProjectVo.setStatus(CrowdConstant.PROJECT_STATUS_ING_NUMBER);
            }else {
                searchProjectVo.setStatus(null);
            }
            projectPagesRemote = mysqlRemoteService.getProjectPagesRemote(page, size, searchProjectVo);
        }
        if (!projectPagesRemote.isSuccess()) {
            return ResultEntity.fail();
        }

        Page<SimpleProject> simpleProjectPage = projectPagesRemote.getData();
        Page<SimpleProjectVo> simpleProjectVoPage = new Page<>(page,size);

        BeanUtils.copyProperties(simpleProjectVoPage, simpleProjectVoPage);
        simpleProjectVoPage.setTotal(simpleProjectPage.getTotal());


        List<SimpleProjectVo> simpleProjectVos = new ArrayList<>();
        for (SimpleProject simpleProject : simpleProjectPage.getRecords()) {
            User user = simpleProject.getUser();
            SimpleUserVo simpleUserVo = new SimpleUserVo();
            BeanUtils.copyProperties(user, simpleUserVo);
            SimpleProjectVo simpleProjectVo = new SimpleProjectVo();
            BeanUtils.copyProperties(simpleProject, simpleProjectVo);
            simpleProjectVo.setSimpleUserVo(simpleUserVo);
            simpleProjectVos.add(simpleProjectVo);
        }
        simpleProjectVoPage.setRecords(simpleProjectVos);

        return ResultEntity.success(simpleProjectVoPage);


    }

    @ApiOperation("查看项目详情")
    @GetMapping("/get/project/detail/{project_id}")
    public ResultEntity<ProjectVo> getProjectDetail(@PathVariable("project_id") String projectId) {
        ResultEntity<Project> projectByProjectIdRemote = mysqlRemoteService.getProjectByProjectIdRemote(projectId);

        if (!projectByProjectIdRemote.isSuccess()) {
            return ResultEntity.fail();
        }

        Project project = projectByProjectIdRemote.getData();
        if (null == project) {
            return ResultEntity.fail();
        }
        ProjectVo projectVo = projectToProjectVo(project);

        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(project.getSponsor());
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.fail();
        }
        User user = userByUidRemote.getData();
        if (null != user) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            projectVo.setSponsor(userVo);
        }


        return ResultEntity.success(projectVo);
    }


    private ProjectVo projectToProjectVo(Project project) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        List<Reward> rewards = project.getRewards();
        // 项目回报
        if (null != rewards && rewards.size() != 0) {
            List<RewardVo> rewardVos = new ArrayList<>();
            rewards.forEach(reward -> {
                RewardVo rewardVo = new RewardVo();
                BeanUtils.copyProperties(reward, rewardVo);

                List<Picture> pictures = reward.getPicture();

                if (null != pictures && pictures.size() != 0) {
                    List<PictureVo> pictureVos = new ArrayList<>();

                    pictures.forEach(picture -> {
                        PictureVo pictureVo = new PictureVo();
                        BeanUtils.copyProperties(picture, pictureVo);
                        pictureVos.add(pictureVo);
                    });
                    rewardVo.setPictureVos(pictureVos);
                }
                rewardVos.add(rewardVo);
            });
            projectVo.setRewardVos(rewardVos);
            // 项目详情
            List<ProjectDetail> projectDetails = project.getProjectDetails();
            if (null != projectDetails && projectDetails.size() != 0) {
                List<ProjectDetailVo> projectDetailVoList = new ArrayList<>();
                projectDetails.forEach(projectDetail -> {
                    ProjectDetailVo projectDetailVo = new ProjectDetailVo();
                    BeanUtils.copyProperties(projectDetail, projectDetailVo);
                    projectDetailVoList.add(projectDetailVo);
                });
                projectVo.setProjectDetailVos(projectDetailVoList);
            }

            // 项目辅助资料
            List<Picture> projectSupportingList = project.getProjectSupportingList();
            if (null != projectSupportingList && projectSupportingList.size() != 0) {
                List<PictureVo> projectSupportingVoList = new ArrayList<>();
                projectSupportingList.forEach(projectSupporting -> {
                    PictureVo projectSupportingVo = new PictureVo();
                    BeanUtils.copyProperties(projectSupporting, projectSupportingVo);
                    projectSupportingVoList.add(projectSupportingVo);
                });
                projectVo.setProjectSupportingVoList(projectSupportingVoList);
            }
        }
        return projectVo;
    }

    // 发布项目评论
    @PostMapping("/add/comment/to/project")
    @ApiOperation("发布项目评论")
    public ResultEntity<String> addCommentToProject(@RequestBody CommentFormVo commentFormVo, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        commentFormVo.setSourceType(CrowdConstant.COMMENT_TYPE_PROJECT);
        ResultEntity<String> stringResultEntity = mysqlRemoteService.addComment(commentFormVo);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.fail(stringResultEntity.getMessage());
        }
        return ResultEntity.success();
    }

    @DeleteMapping("/remove/comment/by/{commentId}")
    @ApiOperation("删除评论")
    public ResultEntity<String> removeCommentById(@PathVariable String commentId, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        ResultEntity<String> stringResultEntity = mysqlRemoteService.removeComment(commentId, userId);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.fail(stringResultEntity.getMessage());
        }
        return ResultEntity.success();
    }

    @GetMapping("/get/comments/by/{projectId}")
    @ApiOperation("获取项目评论")
    public ResultEntity<Map<String, Object>> getCommentsByProjectId(@PathVariable String projectId, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        ResultEntity<Map<String, Object>> stringResultEntity = mysqlRemoteService.getCommentList(projectId, CrowdConstant.COMMENT_TYPE_PROJECT);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.fail();
        }
        return ResultEntity.success(stringResultEntity.getData());
    }
}