package com.wj.crowd.api.mysql;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.*;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import com.wj.crowd.entity.Vo.project.UpdateProjectVo;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/5/9 - 14:42
 */
@Service
@FeignClient("crowd-mysql")
public interface MysqlRemoteService {
    // 用户注册登录远程接口
    @GetMapping("/mysql/user/get/user_info/remote/{phone}")
    @ApiOperation("用户手机登录接口")
    ResultEntity<User> getUserByPhoneRemote(@PathVariable String phone);

    @PostMapping("/mysql/user/save/user/remote")
    @ApiOperation("用户注册")
    ResultEntity<String> saveUserRemote(@RequestBody User user);

    @GetMapping("/mysql/user/get/user_info_detail/remote/{uid}")
    @ApiOperation("查询用户详细信息")
    ResultEntity<User> getUserByUidRemote(@PathVariable String uid);

    @PutMapping("/mysql/user/modify/user_info/remote")
    @ApiOperation("更新用户信息")
    ResultEntity<String> modifyUserRemote(@RequestBody User user);

    @PutMapping("/mysql/user/modify/auth_info/remote")
    @ApiOperation("更新认证信息")
    ResultEntity<String> modifyAuthInfoRemote(UserAuthInfoVo userAuthInfoVo);

    @ApiOperation("查询用户动态")
    @GetMapping("/mysql/dynamic/get/dynamic/by/{userId}")
    ResultEntity<List<Dynamic>> getDynamicByUserId(@PathVariable String userId);

    // 项目远程接口

    @PostMapping("/mysql/project/save/project/remote")
    @ApiOperation("新建项目")
    ResultEntity<String> saveProjectRemote(@RequestBody Project project);

    @PostMapping("/mysql/project/get/project/pages/remote/{page}/{size}")
    @ApiOperation("分页带条件查询所有项目")
    ResultEntity<Page<SimpleProject>> getProjectPagesRemote(@PathVariable Long page,
                                                            @PathVariable Long size,
                                                            @RequestBody(required = false) SearchProjectVo searchProjectVo);

    @GetMapping("/mysql/project/get/project/detail/remote/{project_id}")
    @ApiOperation("根据id查询项目详细信息")
    ResultEntity<Project> getProjectByProjectIdRemote(@PathVariable("project_id") String projectId);

    @PutMapping("/mysql/project/modify/project/remote")
    @ApiOperation("根据id更新项目详细信息")
    ResultEntity<String> modifyProjectRemote(@RequestBody UpdateProjectVo updateProjectVo);

    // 用户动态远程接口
    @PostMapping("/mysql/dynamic/save/dynamic")
    @ApiOperation("发布动态")
    ResultEntity<String> saveDynamic(@RequestBody Dynamic dynamic);

    @ApiOperation("用户删除动态")
    @DeleteMapping("/mysql/dynamic/remove/dynamic/{uid}/{dynamicId}")
    ResultEntity<String> removeDynamic(@PathVariable String uid, @PathVariable String dynamicId);

    // 用户收货地址管理
    @GetMapping("/mysql/shipping-address/get/shipping/address/{uid}")
    @ApiOperation("获取用户所有地址信息")
    ResultEntity<List<ShippingAddress>> getShippingAddress(@PathVariable String uid);

    @PutMapping("/mysql/shipping-address/modify/shipping/address")
    @ApiOperation("修改用户地址信息")
    ResultEntity<String> modifyShippingAddress(@RequestBody ShippingAddress shippingAddress);

    @PostMapping("/mysql/shipping-address/save/shipping/address")
    @ApiOperation("新建收货地址")
    ResultEntity<String> saveShippingAddress(@RequestBody ShippingAddress shippingAddress);

    @DeleteMapping("/mysql/shipping-address/remove/shipping/address/{addressId}")
    @ApiOperation("删除收货地址")
    ResultEntity<String> removeShippingAddress(@PathVariable String addressId);
}
