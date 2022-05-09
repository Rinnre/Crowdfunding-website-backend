package com.wj.crowd.management.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.management.entity.Do.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.management.entity.Vo.PermissionTreeVo;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.entity.Vo.RoleVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-04-11
 */
public interface RoleService extends IService<Role> {

    IPage<List<RoleVo>> getRolePages(Page rolePage, String roleName);

    RoleVo getRoleById(String id);

    void modifyRole(String id, RoleVo roleVo);

    void saveRole(RoleVo roleVo);

    List<PermissionVo> getAssignedPermissions(String roleId);

    void assignPermission(String roleId, List<String> permissionIdList);

    void unAssignPermission(String roleId, List<String> permissionIdList);

    List<RoleVo> getRoles();

    List<PermissionTreeVo> getPermissionsTree();


}
