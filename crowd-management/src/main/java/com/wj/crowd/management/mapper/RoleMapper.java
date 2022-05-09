package com.wj.crowd.management.mapper;

import com.wj.crowd.management.entity.Do.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author w
 * @since 2022-04-11
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getAssignedRoles(String id);

    void assignRoles(String adminId, List<String> roleIdList);

    void unAssignRoles(String adminId, List<String> roleIdList);
}
