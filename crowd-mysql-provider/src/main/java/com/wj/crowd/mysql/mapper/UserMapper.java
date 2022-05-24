package com.wj.crowd.mysql.mapper;

import com.wj.crowd.entity.Do.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author w
 * @since 2022-05-06
 */
public interface UserMapper extends BaseMapper<User> {
    User getUserById(@Param("sponsor") String id);
}
