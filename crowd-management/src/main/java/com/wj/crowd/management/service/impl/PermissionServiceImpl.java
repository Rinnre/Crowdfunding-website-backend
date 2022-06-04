package com.wj.crowd.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.crowd.common.constant.PermissionValueToName;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.management.entity.Do.Permission;
import com.wj.crowd.management.entity.Do.Role;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.entity.Vo.RoleVo;
import com.wj.crowd.management.mapper.PermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.management.service.api.AdminService;
import com.wj.crowd.management.service.api.PermissionService;
import com.wj.crowd.management.service.api.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-04-20
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    /**
     * 获取所有权限
     *
     * @return
     */
    @Override
    public List<PermissionVo> getPermissions() {
        List<Permission> permissions = baseMapper.selectList(null);
        List<PermissionVo> permissionVos = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            permissionVos.add(permissionVo);
        }
        return permissionVos;
    }

    @Override
    public List<Permission> getPermissionsByMenuId(String id) {
        if (null == id) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("menu_id", id);
        return baseMapper.selectList(permissionQueryWrapper);
    }

    @Override
    public int removePermissionByMenuId(String menuId) {
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("menu_id", menuId);
        return baseMapper.delete(permissionQueryWrapper);
    }

    @Override
    public int removePermissionMenuByMenuId(String menuId) {
        return baseMapper.deletePermissionMenuByMenuId(menuId);

    }

    @Override
    public List<PermissionVo> getPermissions(String menuId) {
        List<Permission> permissions = baseMapper.getMenuPermissionsById(menuId);
        List<PermissionVo> permissionVos = new ArrayList<>();
        // 封装vo对象
        permissions.forEach(permission -> {
            PermissionVo permissionVo = new PermissionVo();
            String value = permission.getValue().split("\\.")[1].toUpperCase();
            permissionVo.setPermissionName(PermissionValueToName.getName(value));
            BeanUtils.copyProperties(permission, permissionVo);
            permissionVos.add(permissionVo);
        });
        return permissionVos;
    }

    @Override
    public List<String> selectPermissionValueListByUserId(String id) {
        List<RoleVo> assignedRoles = adminService.getAssignedRoles(id);
        List<String> permissionValues = new ArrayList<>();
        if(assignedRoles!=null){

            assignedRoles.forEach(roleVo -> {
                List<PermissionVo> assignedPermissions = roleService.getAssignedPermissions(roleVo.getId());
                assignedPermissions.forEach(assignedPermission -> {
                    permissionValues.add(assignedPermission.getValue());
                });
            });
        }

        return permissionValues;
    }
}
