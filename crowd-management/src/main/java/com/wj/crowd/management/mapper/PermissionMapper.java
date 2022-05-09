package com.wj.crowd.management.mapper;

import com.wj.crowd.management.entity.Do.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author w
 * @since 2022-04-20
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> getAssignedPermissions(String roleId);

    void assignPermission(String roleId, List<String> permissionIdList);

    void unAssignPermission(String roleId, List<String> permissionIdList);

    List<Permission> getMenuPermissionsById(String id);

    int deletePermissionMenuByMenuId(String menuId);
}
