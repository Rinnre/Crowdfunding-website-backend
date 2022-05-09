package com.wj.crowd.management.controller;


import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.management.entity.Vo.MenuVo;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.service.api.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author w
 * @since 2022-04-11
 */
@CrossOrigin
@Api("菜单权限管理模块")
@RestController
@RequestMapping("/management/menu-permissions")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/get/menu/tree")
    @ApiOperation("获取菜单树根节点")
    public ResultEntity<MenuVo> getMenuTree(){
        try {
            MenuVo menuVo = menuService.getMenuTree();
            return ResultEntity.success(menuVo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PostMapping("/save/menu")
    @ApiOperation("保存菜单节点")
    public ResultEntity<String> saveMenu(@RequestBody MenuVo menuVo){
        try {
            menuService.saveMenu(menuVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PutMapping("/modify/menu")
    @ApiOperation("修改菜单节点")
    public ResultEntity<String> modifyMenu(@RequestBody MenuVo menuVo){
        try {
            menuService.modifyMenu(menuVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @DeleteMapping("/remove/menu/{menu_id}")
    @ApiOperation("删除菜单节点")
    public ResultEntity<String> removeMenu(@PathVariable("menu_id") String menuId){
        try {
            menuService.removeMenu(menuId);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }
    @ApiOperation("页面节点对应的所有权限")
    @GetMapping("/get/permissions/{menu_id}")
    public ResultEntity<List<PermissionVo>> getPermissions(@PathVariable("menu_id") String menuId){
        try {
            List<PermissionVo> permissionVos = menuService.getPermissions(menuId);
            return ResultEntity.success(permissionVos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation("获取已经分配给页面的权限")
    @GetMapping("/get/assigned/permissions/{menu_id}")
    public ResultEntity<List<PermissionVo>> getAssignedPermissions(@PathVariable("menu_id") String menuId){
        try {
            List<PermissionVo> permissionVos = menuService.getAssignedPermissions(menuId);
            return ResultEntity.success(permissionVos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PostMapping("/assign/permissions/{menu_id}")
    @ApiOperation("菜单分配对应的所需权限")
    public ResultEntity<String> assignPermission(@PathVariable("menu_id") String menuId,
                                                 @RequestBody List<String> permissionIdList){
        try {
            menuService.assignPermission(menuId,permissionIdList);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/unAssign/permissions/{menu_id}")
    @ApiOperation("菜单取消分配对应的所需权限")
    public ResultEntity<String> unAssignPermission(@PathVariable("menu_id") String menuId,
                                                 @RequestBody List<String> permissionIdList){
        try {
            menuService.unAssignPermission(menuId,permissionIdList);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }


}

