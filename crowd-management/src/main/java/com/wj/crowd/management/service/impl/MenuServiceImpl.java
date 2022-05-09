package com.wj.crowd.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.crowd.common.constant.PermissionValueToName;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.management.entity.Do.Menu;
import com.wj.crowd.management.entity.Do.Permission;
import com.wj.crowd.management.entity.Vo.MenuVo;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.mapper.MenuMapper;
import com.wj.crowd.management.service.api.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.management.service.api.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private PermissionService permissionService;

    /**
     * 查询菜单-权限树
     * 并缓存菜单属性结构
     *
     * @return
     */
    @Override

    public MenuVo getMenuTree() {
        // 查询所有数据
        List<Menu> menuList = baseMapper.getAllMenu();

        if (null == menuList) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 分离出顶级父类Vo对象
        List<MenuVo> fatherMenuVoList = new ArrayList<>();
        for (Menu menu : menuList) {
            if ("0".equals(menu.getPid())) {
                MenuVo menuVo = caseMenuDoToMenuVo(menu);
                fatherMenuVoList.add(menuVo);
            }
        }

        if (fatherMenuVoList.size() <= 0) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 递归生成菜单权限树
        findSubCategory(fatherMenuVoList, menuList);

        return fatherMenuVoList.get(0);
    }

    /**
     * 新增菜单节点、自动新增对应权限等待分配
     * 开启事务、防止新增失败
     *
     * @param menuVo
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveMenu(MenuVo menuVo) {
        // 数据转换
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuVo, menu);

        // 保存菜单数据
        int insert = baseMapper.insert(menu);
//        log.info(menu.getId());

        if (insert < 0) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }

        // 生成权限
        List<Permission> permissions = generatePermissions(menu.getPath(), menu.getId());

        // 判断菜单节点是否可以生成权限
        if (permissions != null && menuVo.getType() ==1) {

            // 保存权限
            boolean saveBatchResult = permissionService.saveBatch(permissions);
            if (!saveBatchResult) {
                throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
            }
        }
    }

    /**
     * 修改菜单节点、重新生成权限
     *
     * @param menuVo
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void modifyMenu(MenuVo menuVo) {
        // 数据验证
        String id = menuVo.getId();
        Menu menu = baseMapper.selectById(id);

        if (null == id || null == menu) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        String name = menuVo.getName();
        String path = menuVo.getPath();
        String icon = menuVo.getIcon();

        if (!ObjectUtils.isEmpty(name)) {
            menu.setName(name);
        }
        if (!ObjectUtils.isEmpty(path)) {
            menu.setPath(path);
        }
        if (!ObjectUtils.isEmpty(icon)) {
            menu.setIcon(icon);
        }

        menu.setUpdateTime(LocalDateTime.now());


        // 保存菜单数据
        int insert = baseMapper.updateById(menu);
        if (insert < 0) {
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }


        // 更新权限值
        List<Permission> permissions = permissionService.getPermissionsByMenuId(id);

        if (null == path) {
            path = menu.getPath();
        }

        String[] stringPath = path.split("//");

        // 判断菜单节点是否可以分配操作权限
        if (stringPath.length - 2 > 0 && menuVo.getType() == 1) {

            String prePermission = stringPath[stringPath.length - 2];

            permissions.forEach(permission -> {
                String sufPermission = permission.getValue().split("\\.")[1];
                permission.setValue(prePermission + "." + sufPermission);
            });


            // 更新
            boolean saveBatchResult = permissionService.updateBatchById(permissions);
            if (saveBatchResult) {
                throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
            }
        }
    }

    /**
     * 删除菜单节点、菜单所绑定权限、以及权限
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void removeMenu(String menuId) {

        // 若删除节点有子节点则不允许删除
        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("pid", menuId);
        Long children = baseMapper.selectCount(menuQueryWrapper);
        if (children > 0) {
            throw new CrowdException(ResultCodeEnum.DATA_DELETE_ERROR_HAS_CHILDREN);
        }
        // 操作菜单表
        int menuDeleteResult = baseMapper.deleteById(menuId);
        if (menuDeleteResult <= 0) {
            throw new CrowdException(ResultCodeEnum.DELETE_DATA_ERROR);
        }

        // 操作权限表
        int permissionDeleteResult = permissionService.removePermissionByMenuId(menuId);

//        if (permissionDeleteResult <= 0) {
//            throw new CrowdException(ResultCodeEnum.DELETE_DATA_ERROR);
//        }

        // 操作菜单权限表
        int permissionMenuDeleteResult = permissionService.removePermissionMenuByMenuId(menuId);
        if (permissionMenuDeleteResult < 0) {
            throw new CrowdException(ResultCodeEnum.DELETE_DATA_ERROR);
        }

    }

    /**
     * 查询页面所有权限
     *
     * @param menuId
     * @return
     */
    @Override
    public List<PermissionVo> getPermissions(String menuId) {
        // 查询数据
        List<Permission> permissions = permissionService.getPermissionsByMenuId(menuId);
        // 封装vo对象集合
        List<PermissionVo> permissionVos = new ArrayList<>();
        permissions.forEach(permission -> {
            PermissionVo permissionVo = casePermissionDoToPermissionVo(permission);
            permissionVos.add(permissionVo);
        });
        return permissionVos;
    }

    /**
     * 查询已经分配的权限
     *
     * @param menuId
     * @return
     */
    @Override
    public List<PermissionVo> getAssignedPermissions(String menuId) {
        List<Permission> permissions = baseMapper.getAssignedPermissions(menuId);
        List<PermissionVo> permissionVos = new ArrayList<>();
        permissions.forEach(permission -> {
            PermissionVo permissionVo = casePermissionDoToPermissionVo(permission);
            permissionVos.add(permissionVo);
        });
        return permissionVos;
    }

    @Override
    public void assignPermission(String menuId, List<String> permissionIdList) {
        baseMapper.assignPermission(menuId, permissionIdList);
    }

    @Override
    public void unAssignPermission(String menuId, List<String> permissionIdList) {
        baseMapper.unAssignPermission(menuId, permissionIdList);
    }

    /**
     * 自动生成权限
     *
     * @return
     */
    private List<Permission> generatePermissions(String path, String menuId) {

        List<Permission> permissions = new ArrayList<>();
        List<String> sufPermissions = PermissionValueToName.getSufPermission();

        String[] pathString = path.split("/");
        if (pathString.length - 2 < 0) {
            return null;
        }

        // 遍历权限后缀并拼接权限、添加到权限集合中
        sufPermissions.forEach(sufPermission -> {
            StringBuffer prePermission = new StringBuffer(pathString[pathString.length - 2]);
            Permission permission = new Permission();
            permission.setValue(prePermission.append(".").append(sufPermission).toString());
            permission.setMenuId(menuId);
            permissions.add(permission);
        });
        return permissions;
    }


    /**
     * 递归查询并组装子节点
     *
     * @param fatherMenuVoList 父类节点集合
     * @param menuList         所有节点集合
     */
    private void findSubCategory(List<MenuVo> fatherMenuVoList, List<Menu> menuList) {

        // 遍历所有父类节点
        for (MenuVo fatherMenuVo : fatherMenuVoList) {

            // 组装每一个父节点的子节点集合
            List<MenuVo> childrenMenuPermissionVoList = new ArrayList<>();

            for (Menu menu : menuList) {
                // 封装子节点vo对象
                if (Objects.equals(menu.getPid(), fatherMenuVo.getId())) {
                    MenuVo menuVo = caseMenuDoToMenuVo(menu);
                    childrenMenuPermissionVoList.add(menuVo);
                }
            }
            // 进行id升序排序
            childrenMenuPermissionVoList.sort(new Comparator<MenuVo>() {
                @Override
                public int compare(MenuVo o1, MenuVo o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
            fatherMenuVo.setChildren(childrenMenuPermissionVoList);
            // 进行递归组装
            findSubCategory(childrenMenuPermissionVoList, menuList);
        }

    }

    /**
     * 封装Vo对象
     *
     * @param menu Do对象
     * @return Vo对象
     */
    private MenuVo caseMenuDoToMenuVo(Menu menu) {
        MenuVo menuVo = new MenuVo();
        BeanUtils.copyProperties(menu, menuVo);

        // 封装权限VO对象集合
        List<PermissionVo> permissionVos = new ArrayList<>();

        if(menu.getType()==1){

            menu.getPermissions().forEach(permission -> {
                PermissionVo permissionVo = casePermissionDoToPermissionVo(permission);
                permissionVos.add(permissionVo);
            });
        }
        // 存回菜单vo对象
        menuVo.setPermissionValue(permissionVos);
        return menuVo;
    }

    /**
     * permission - > permissionVo
     *
     * @param permission
     * @return
     */
    private PermissionVo casePermissionDoToPermissionVo(Permission permission) {
        PermissionVo permissionVo = new PermissionVo();
        String[] value = permission.getValue().split("\\.");
        if(value.length<2){
            return permissionVo;
        }
        String name = value[1].toUpperCase();
        permissionVo.setPermissionName(PermissionValueToName.getName(name));
        BeanUtils.copyProperties(permission, permissionVo);
        return permissionVo;
    }
}
