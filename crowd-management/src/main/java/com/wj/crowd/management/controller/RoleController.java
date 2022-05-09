package com.wj.crowd.management.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.management.entity.Do.Role;
import com.wj.crowd.management.entity.Vo.PermissionTreeVo;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.entity.Vo.RoleVo;
import com.wj.crowd.management.service.api.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

/**
 *
 * @author w
 * @since 2022-04-11
 */
@CrossOrigin
@Api("角色管理模块")
@RestController
@RequestMapping("/management/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("/get/role/pages/{page}/{size}")
    @ApiOperation("条件查询所有角色带分页")
    public ResultEntity<IPage<List<RoleVo>>> getRolePages(@PathVariable Long page,
                                                    @PathVariable Long size,
                                                    @RequestParam(value = "key_words",required = false) String keyWords){

        try {
            Page rolePage = new Page(page, size);
            IPage<List<RoleVo>> roleVoIPage = roleService.getRolePages(rolePage,keyWords);
            return ResultEntity.success(roleVoIPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @GetMapping("/get/role/by/{id}")
    @ApiOperation("根据id查询角色信息")
    public ResultEntity<RoleVo> getRoleById(@PathVariable String id){
        RoleVo roleVo = roleService.getRoleById(id);
        return ResultEntity.success(roleVo);
    }

    @PutMapping("/modify/role/{id}")
    @ApiOperation("根据id更新角色信息")
    public ResultEntity<String> modifyRole(@PathVariable String id,
                                           @RequestBody RoleVo roleVo){
        try {
            roleService.modifyRole(id,roleVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/save/role")
    @ApiOperation("新建角色")
    public ResultEntity<String> saveRole(@RequestBody RoleVo roleVo){
        roleService.saveRole(roleVo);
        return ResultEntity.success();
    }

    @DeleteMapping("/remove/role/{id}")
    @ApiOperation("根据id删除角色信息")
    public ResultEntity<String> removeRole(@PathVariable String id){
        boolean removeResult = roleService.removeById(id);
        return removeResult?ResultEntity.success():ResultEntity.fail();
    }

    @DeleteMapping("/remove/role/list")
    @ApiOperation("批量删除角色信息")
    public ResultEntity<String> removeRoleList(@RequestBody List<String> idList){
        try {
            boolean removeResult = roleService.removeBatchByIds(idList);
            return removeResult?ResultEntity.success():ResultEntity.fail();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @GetMapping("/get/permissions/tree")
    @ApiOperation("获取权限树")
    public ResultEntity<List<PermissionTreeVo>> getPermissionsTree(){
        try {
            List<PermissionTreeVo> permissionTreeVo =roleService.getPermissionsTree();
            return ResultEntity.success(permissionTreeVo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PostMapping("/assign/permissions/{role_id}")
    @ApiOperation("角色分配对应的权限")
    public ResultEntity<String> assignPermission(@PathVariable("role_id") String roleId,
                                                 @RequestBody List<String> permissionIdList){
        try {
            roleService.assignPermission(roleId,permissionIdList);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @DeleteMapping("/unAssign/permissions/{role_id}")
    @ApiOperation("取消分配给角色的权限")
    public ResultEntity<String> unAssignPermission(@PathVariable("role_id") String roleId,
                                                   @RequestBody List<String> permissionIdList){
        try {
            roleService.unAssignPermission(roleId,permissionIdList);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @GetMapping("/get/assigned/permissions/{role_id}")
    @ApiOperation("获取角色分配的权限")
    public ResultEntity<List<PermissionVo>> getAssignedPermissions(@PathVariable("role_id") String roleId){
        try {
            List<PermissionVo> permissionVos = roleService.getAssignedPermissions(roleId);
            return ResultEntity.success(permissionVos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @GetMapping("/get/Roles")
    @ApiOperation("获取所有角色")
    public ResultEntity<List<RoleVo>> getRoles(){
        try {
            List<RoleVo> roles = roleService.getRoles();
            return ResultEntity.success(roles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

}

