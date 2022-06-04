package com.wj.crowd.management.service.api;

import com.wj.crowd.management.entity.Do.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.management.entity.Vo.PermissionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-04-20
 */
public interface PermissionService extends IService<Permission> {

    List<PermissionVo> getPermissions();

    List<Permission> getPermissionsByMenuId(String id);

    int removePermissionByMenuId(String menuId);

    int removePermissionMenuByMenuId(String menuId);

    List<PermissionVo> getPermissions(String menuId);

    List<String> selectPermissionValueListByUserId(String id);
}
