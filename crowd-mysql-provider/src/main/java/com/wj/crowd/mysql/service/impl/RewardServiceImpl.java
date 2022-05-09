package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.entity.Do.Reward;
import com.wj.crowd.mysql.mapper.RewardMapper;
import com.wj.crowd.mysql.service.api.RewardService;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 16:21
 */
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {
}
