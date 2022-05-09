package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Do.ProjectDetail;
import com.wj.crowd.entity.Do.Reward;
import com.wj.crowd.entity.Do.SimpleProject;
import com.wj.crowd.entity.Vo.project.ProjectDetailVo;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import com.wj.crowd.entity.Vo.user.UpdateProjectVo;
import com.wj.crowd.mysql.mapper.ProjectMapper;
import com.wj.crowd.mysql.service.api.ProjectDetailService;
import com.wj.crowd.mysql.service.api.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.mysql.service.api.RewardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-05-05
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private ProjectDetailService projectDetailService;

    /**
     * 开启事务
     * 保存新项目
     *
     * @param project 项目信息
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveProject(Project project) {

        // 保存项目基本信息、 获取自动生成的项目id
        int saveProjectResult = baseMapper.insert(project);

        // 保存失败
        if (saveProjectResult <= 0) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }

        // 根据项目id保存回报信息和项目详细信息
        String id = project.getId();

        List<Reward> rewards = project.getRewards();

        List<ProjectDetail> projectDetails = project.getProjectDetails();

        // 完善回报信息
        rewards.forEach(reward -> {
            reward.setProjectId(id);
        });

        // 保存回报信息
        boolean saveRewardResult = rewardService.saveBatch(rewards);
        if (!saveRewardResult) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }

        // 完善项目详情信息
        projectDetails.forEach(projectDetail -> {
            projectDetail.setProjectId(id);
        });

        // 保存项目详细信息
        boolean saveProjectDetailResult = projectDetailService.saveBatch(projectDetails);

        if (!saveProjectDetailResult) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }

    }

    /**
     * 主页所需数据
     *
     * @param page            当前页码
     * @param size            一页多少数据
     * @param searchProjectVo 查询条件封装对象
     * @return 条件查询带分页项目数据
     */
    @Override
    public Page<SimpleProject> getProjectPages(Long page, Long size, SearchProjectVo searchProjectVo) {
        List<SimpleProject> simpleProjects = baseMapper.getProjectPages(page - 1, size, searchProjectVo);

        // 手动设置分页数据
        Page<SimpleProject> simpleProjectPage = new Page<>();
        simpleProjectPage.setRecords(simpleProjects);
        // 设置当前页
        simpleProjectPage.setCurrent(page);

        // 查询总页数
        Long projectPagesCount = baseMapper.getProjectPagesCount(searchProjectVo);
        simpleProjectPage.setTotal(projectPagesCount);
        simpleProjectPage.setSize(size);

        // 设置页码数量
        simpleProjectPage.setPages(projectPagesCount / size + 1);

        return simpleProjectPage;
    }

    /**
     * 项目详情信息
     *
     * @param projectId 项目id
     * @return 项目详细信息
     */
    @Override
    public Project getProjectByProjectId(String projectId) {
        Project project = baseMapper.getProjectByProjectId(projectId);
        return project;
    }

    /**
     * 更新项目信息
     *
     * @param projectVo
     */
    @Override
    public void modifyProjectRemote(UpdateProjectVo projectVo) {
        String id = projectVo.getId();

        Project project = baseMapper.selectById(id);
        if (null == project) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 更新标题
        String title = projectVo.getTitle();
        if (null != title) {
            project.setTitle(title);
        }
        // 更新项目名称
        String name = projectVo.getName();
        if (null != name) {
            project.setName(name);
        }
        // 更新项目类型
        String type = projectVo.getType();
        if (null != type) {
            project.setType(type);
        }
        // 更新项目头图
        String headPicture = projectVo.getHeadPicture();
        if (null != headPicture) {
            project.setHeadPicture(headPicture);
        }
        // 更新项目推荐图
        String adPicture = projectVo.getAdPicture();
        if (null != adPicture) {
            project.setAdPicture(adPicture);
        }
        // 更新项目视频
        String video = projectVo.getVideo();
        if (null != video) {
            project.setVideo(video);
        }

        // 更新数据库数据
        int updateResult = baseMapper.updateById(project);
        if (updateResult <= 0) {
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }
        // 更新项目详情

        List<ProjectDetailVo> projectDetailVos = projectVo.getProjectDetailVos();
        // projectDetailVo -> projectDetail
        List<ProjectDetail> projectDetails = new ArrayList<>();

        projectDetailVos.forEach(projectDetailVo -> {
            ProjectDetail projectDetail = new ProjectDetail();
            BeanUtils.copyProperties(projectDetailVo,projectDetail);
            projectDetails.add(projectDetail);
        });

        if ( projectDetails.size() > 0) {

            projectDetailService.updateBatchById(projectDetails);
        }


    }
}
