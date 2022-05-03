package com.wj.crowd.management.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.management.entity.Do.Admin;
import com.wj.crowd.management.entity.Vo.AdminVo;
import com.wj.crowd.management.entity.Vo.RoleVo;
import com.wj.crowd.management.service.api.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author w
 * @since 2022-04-10
 */
@CrossOrigin
@Api("管理员管理模块")
@RestController
@RequestMapping("/management/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页显示所有管理员信息")
    @GetMapping("/get/admin/pages/{page}/{size}")
    public ResultEntity<IPage<AdminVo>> getAdminPages(@PathVariable Long page,
                                                      @PathVariable Long size,
                                                      @RequestParam(value = "key_words",required = false) String keyWords) {
        Page<Admin> adminPage = new Page<>(page, size);
        IPage<AdminVo> adminPages = null;
        try {
            adminPages = adminService.getAdminPages(adminPage, keyWords);
            return ResultEntity.success(adminPages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @GetMapping("/get/admin/{id}")
    @ApiOperation("根据管理员Id查询信息")
    public ResultEntity<AdminVo> getAdminById(@PathVariable String id) {
        AdminVo adminVo = null;
        try {
            adminVo = adminService.getAdminById(id);
            return ResultEntity.success(adminVo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PutMapping("/modify/admin/{id}")
    @ApiOperation("修改管理员信息")
    public ResultEntity<String> modifyAdminById(@PathVariable String id,
                                                @RequestBody AdminVo adminVo) {
        try {
            adminService.modifyAdminById(id, adminVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @DeleteMapping("/remove/admin/{id}")
    @ApiOperation("根据id删除管理员账号")
    public ResultEntity<String> removeAdminById(@PathVariable String id) {
        try {
            boolean removeResult = adminService.removeById(id);
            return removeResult ? ResultEntity.success() : ResultEntity.fail();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @DeleteMapping("/remove/admin/list")
    @ApiOperation("批量删除管理员账号")
    public ResultEntity<String> removeAdminByIdList(@RequestBody List<String> idList) {
        try {
            boolean removeResult = adminService.removeBatchByIds(idList);
            return removeResult ? ResultEntity.success() : ResultEntity.fail();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }


    @PostMapping("/save/admin")
    @ApiOperation("添加管理员账号")
    public ResultEntity<String> saveAdmin(@RequestBody AdminVo adminVo) {
        try {
            adminService.saveAdmin(adminVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/assign/roles/{admin_id}")
    @ApiOperation("给管理员账号分配角色")
    public ResultEntity<String> assignRoles(@PathVariable("admin_id") String adminId,
                                            @RequestBody List<String> RoleIdList){
        try {
            adminService.assignRoles(adminId,RoleIdList);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }
    @DeleteMapping("/unAssign/roles/{admin_id}")
    @ApiOperation("取消分配角色")
    public ResultEntity<String> unAssignRoles(@PathVariable("admin_id") String adminId,
                                              @RequestBody List<String> RoleIdList){
        try {
            adminService.unAssignRoles(adminId,RoleIdList);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @GetMapping("/get/assigned/roles/{admin_id}")
    @ApiOperation("获取已经分配的角色")
    public ResultEntity<List<RoleVo>> getAssignedRoles (@PathVariable("admin_id") String id){
        try {
            List<RoleVo> roles = adminService.getAssignedRoles(id);
            return ResultEntity.success(roles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }

    }


}

