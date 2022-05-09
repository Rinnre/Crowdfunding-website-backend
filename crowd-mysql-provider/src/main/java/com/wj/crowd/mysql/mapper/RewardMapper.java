package com.wj.crowd.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.crowd.entity.Do.Reward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/6 - 16:12
 */
public interface RewardMapper extends BaseMapper<Reward> {
    List<Reward> getRewardsByProjectId(@Param("id") String projectId);
}
