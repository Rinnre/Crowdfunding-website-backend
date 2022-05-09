package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.entity.Do.ProjectDetail;
import com.wj.crowd.mysql.mapper.ProjectDetailMapper;
import com.wj.crowd.mysql.service.api.ProjectDetailService;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 16:33
 */
@Service
public class ProjectDetailServiceImpl extends ServiceImpl<ProjectDetailMapper, ProjectDetail> implements ProjectDetailService {
}
