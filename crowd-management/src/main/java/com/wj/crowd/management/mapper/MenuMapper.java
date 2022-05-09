package com.wj.crowd.management.mapper;

import com.wj.crowd.management.entity.Do.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wj.crowd.management.entity.Do.Permission;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author w
 * @since 2022-04-11
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getAllMenu();

    List<Permission> getAssignedPermissions(String menuId);

    void assignPermission(String menuId, List<String> permissionIdList);

    void unAssignPermission(String menuId, List<String> permissionIdList);
}
