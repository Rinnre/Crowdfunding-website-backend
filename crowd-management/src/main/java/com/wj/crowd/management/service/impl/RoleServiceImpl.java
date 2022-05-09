package com.wj.crowd.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.constant.PermissionValueToName;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.management.entity.Do.Menu;
import com.wj.crowd.management.entity.Do.Permission;
import com.wj.crowd.management.entity.Do.Role;
import com.wj.crowd.management.entity.Vo.PermissionTreeVo;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.entity.Vo.RoleVo;
import com.wj.crowd.management.mapper.MenuMapper;
import com.wj.crowd.management.mapper.PermissionMapper;
import com.wj.crowd.management.mapper.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.management.service.api.PermissionService;
import com.wj.crowd.management.service.api.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-04-11
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionService permissionService;

    /**
     * 条件查询角色数据带分页
     *
     * @param rolePage 分页设置
     * @param keyWords 查询条件
     * @return 角色分页数据
     */
    @Override
    public IPage<List<RoleVo>> getRolePages(Page rolePage, String keyWords) {

        // 条件查询带分页
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.like(!ObjectUtils.isEmpty(keyWords), "name", keyWords);
        roleQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "description", keyWords);

        // 排除超级管理员
        roleQueryWrapper.ne("id", "1");

        Page page = baseMapper.selectPage(rolePage, roleQueryWrapper);

        // 封装Vo对象
        List<RoleVo> roleVoList = new ArrayList<>();
        for (Role role : (List<Role>) page.getRecords()) {
            RoleVo roleVo = new RoleVo();
            BeanUtils.copyProperties(role, roleVo);
            roleVoList.add(roleVo);
        }
        IPage<List<RoleVo>> roleVoPages = page.setRecords(roleVoList);

        return roleVoPages;
    }


    /**
     * 根据id查询角色信息
     *
     * @param id 角色id
     * @return 角色信息
     */
    @Override
    public RoleVo getRoleById(String id) {
        // 查询对象
        Role role = baseMapper.selectById(id);

        if (null == role) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }
        // 封装Vo对象
        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(role, roleVo);
        return roleVo;
    }

    /**
     * @param id
     * @param roleVo
     */
    @Override
    public void modifyRole(String id, RoleVo roleVo) {

        // 根据id获取需要修改的对象
        Role role = baseMapper.selectById(id);
        if (null == role) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 更新数据
        BeanUtils.copyProperties(roleVo, role);
        role.setUpdateTime(LocalDateTime.now());

        int updateResult = baseMapper.updateById(role);

        if (updateResult <= 0) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }

    }

    /**
     * @param roleVo
     */
    @Override
    public void saveRole(RoleVo roleVo) {
        // 判断该角色是否已经存在
        String name = roleVo.getName();
        if (null == name) {
            // 数据异常
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();

        roleQueryWrapper.eq("name", name);

        Long countResult = baseMapper.selectCount(roleQueryWrapper);

        if (countResult > 0) {
            // 重复角色
            throw new CrowdException(ResultCodeEnum.ROLE_NAME_ALREADY_EXIST);
        }

        // 封装Do类存入数据库
        Role role = new Role();
        BeanUtils.copyProperties(roleVo, role);

        int insertResult = baseMapper.insert(role);

        if (insertResult < 0) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }

    }

    @Override
    public List<PermissionVo> getAssignedPermissions(String roleId) {
        List<Permission> permissions = permissionMapper.getAssignedPermissions(roleId);
        List<PermissionVo> permissionVos = new ArrayList<>();

        // 封装vo对象
        permissions.forEach(permission -> {
            PermissionVo permissionVo = new PermissionVo();
            BeanUtils.copyProperties(permission, permissionVo);
            permissionVos.add(permissionVo);
        });
        return permissionVos;
    }

    /**
     * 将选中权限分配给指定角色
     *
     * @param roleId           角色id
     * @param permissionIdList 权限IdList
     */
    @Override
    public void assignPermission(String roleId, List<String> permissionIdList) {
        permissionMapper.assignPermission(roleId, permissionIdList);

    }

    /**
     * 取消分配角色权限
     *
     * @param roleId
     * @param permissionIdList
     */
    @Override
    public void unAssignPermission(String roleId, List<String> permissionIdList) {
        permissionMapper.unAssignPermission(roleId, permissionIdList);
    }

    /**
     * 获取所有角色数据
     *
     * @return
     */
    @Override
    public List<RoleVo> getRoles() {
        List<Role> roles = baseMapper.selectList(null);
        List<RoleVo> roleVos = new ArrayList<>();
        for (Role role : roles) {
            // 擦除最高角色
            if (Objects.equals(role.getId(), "1")) {
                continue;
            }
            RoleVo roleVo = new RoleVo();
            BeanUtils.copyProperties(role, roleVo);
            roleVos.add(roleVo);
        }
        return roleVos;
    }

    /**
     * 生成权限树
     *
     * @return
     */
    @Override
    public List<PermissionTreeVo> getPermissionsTree() {
        List<Menu> menuList = menuMapper.getAllMenu();

        List<PermissionTreeVo> fatherPermissionNodes = new ArrayList<>();

        // 遍历分离顶级父节点
        menuList.forEach(menu -> {
            if ("1".equals(menu.getId())) {
                PermissionTreeVo permissionTreeVo = caseMenuToPermissionTreeNode(menu);
                fatherPermissionNodes.add(permissionTreeVo);
            }
        });
        // 递归组装子节点
        findSubPermissionNodes(fatherPermissionNodes, menuList);

        return fatherPermissionNodes;
    }

    private void findSubPermissionNodes(List<PermissionTreeVo> fatherPermissionNodes, List<Menu> menuList) {
        if (fatherPermissionNodes == null || fatherPermissionNodes.size() == 0) {
            return;
        }
        // 遍历所有父节点
        fatherPermissionNodes.forEach(fatherPermissionNode -> {
            List<PermissionTreeVo> childrenPermissionNodes = new ArrayList<>();
            // 遍历所有节点、匹配子节点
            menuList.forEach(menu -> {
                if (fatherPermissionNode.getId().equals(menu.getPid())) {
                    PermissionTreeVo permissionTreeVo = caseMenuToPermissionTreeNode(menu);
                    // 如果为页面节点则将权限也视为子节点
                    if (menu.getType() == 1) {
                        assemblePermissions(menu, permissionTreeVo);
                    }
                    childrenPermissionNodes.add(permissionTreeVo);

                }
            });


            // 根据id排序
            childrenPermissionNodes.sort(new Comparator<PermissionTreeVo>() {
                @Override
                public int compare(PermissionTreeVo o1, PermissionTreeVo o2) {
                    // 调用字符串重写的compareTo方法
                    return o2.getId().compareTo(o1.getId());
                }
            });
            // 防止覆盖权限子节点
            if (childrenPermissionNodes.size() > 0) {

                fatherPermissionNode.setChildren(childrenPermissionNodes);
            }
            // 继续寻找子节点
            findSubPermissionNodes(childrenPermissionNodes, menuList);
        });


    }

    /**
     * 组装 权限
     *
     * @param menu
     * @param permissionTreeVo
     */
    private void assemblePermissions(Menu menu, PermissionTreeVo permissionTreeVo) {
        String id = menu.getId();
        List<Permission> permissions = permissionService.getPermissionsByMenuId(id);
        List<PermissionTreeVo> childrenPermissionNodes = new ArrayList<>();

        permissions.forEach(permission -> {
            // 将权限值转为权限名 加强可读性
            PermissionVo permissionVo = casePermissionDoToPermissionVo(permission);

            PermissionTreeVo childrenPermissionNode = new PermissionTreeVo();
            childrenPermissionNode.setId(permissionVo.getId());
            childrenPermissionNode.setLabel(permissionVo.getPermissionName());

            // 存入子节点集合
            childrenPermissionNodes.add(childrenPermissionNode);
        });
        permissionTreeVo.setChildren(childrenPermissionNodes);

    }

    /**
     * menu -> permissionTreeVo
     *
     * @param menu
     * @return
     */
    private PermissionTreeVo caseMenuToPermissionTreeNode(Menu menu) {
        PermissionTreeVo permissionTreeVo = new PermissionTreeVo();
        permissionTreeVo.setId(menu.getId());
        permissionTreeVo.setLabel(menu.getName());
        return permissionTreeVo;
    }

    /**
     * 封装permissionVO对象
     * 将permissionValue转为前端可观性较强的permissionName
     * user.get -> 查看
     *
     * @param permission
     * @return
     */
    private PermissionVo casePermissionDoToPermissionVo(Permission permission) {
        PermissionVo permissionVo = new PermissionVo();
        String[] value = permission.getValue().split("\\.");
        if (value.length < 2) {
            return permissionVo;
        }
        String name = value[1].toUpperCase();
        permissionVo.setPermissionName(PermissionValueToName.getName(name));
        BeanUtils.copyProperties(permission, permissionVo);
        return permissionVo;
    }

}
