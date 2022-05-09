package com.wj.crowd.management.service.api;

import com.wj.crowd.management.entity.Do.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.management.entity.Vo.MenuVo;
import com.wj.crowd.management.entity.Vo.PermissionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-04-11
 */
public interface MenuService extends IService<Menu> {

    MenuVo getMenuTree();

    void saveMenu(MenuVo menuVo);

    void modifyMenu(MenuVo menuVo);

    void removeMenu(String menuId);

    List<PermissionVo> getPermissions(String menuId);

    List<PermissionVo> getAssignedPermissions(String menuId);

    void assignPermission(String menuId, List<String> permissionIdList);

    void unAssignPermission(String menuId, List<String> permissionIdList);
}
