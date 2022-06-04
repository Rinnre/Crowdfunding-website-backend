package com.wj.crowd.mysql.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.User;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import com.wj.crowd.mysql.service.api.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author w
 * @since 2022-05-07
 */
@RestController
@RequestMapping("/mysql/user")
@Api("用户sql接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get/user_info/remote/{phone}")
    @ApiOperation("用户手机登录接口")
    public ResultEntity<User> getUserByPhoneRemote(@PathVariable String phone) {

        try {
            User user = userService.getUserByPhone(phone);
            return ResultEntity.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PostMapping("/save/user/remote")
    @ApiOperation("用户注册")
    public ResultEntity<String> saveUserRemote(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @GetMapping("/get/user_info_detail/remote/{uid}")
    @ApiOperation("查询用户详细信息")
    public ResultEntity<User> getUserByUidRemote(@PathVariable String uid) {
        try {
            User user = userService.getById(uid);
            return ResultEntity.success(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PutMapping("/modify/user_info/remote")
    @ApiOperation("更新用户信息")
    public ResultEntity<String> modifyUserRemote(@RequestBody User user) {
        try {
            userService.modifyUser(user);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PutMapping("/modify/auth_info/remote")
    @ApiOperation("更新认证信息")
    public ResultEntity<String> modifyAuthInfoRemote(@RequestBody UserAuthInfoVo userAuthInfoVo) {
        try {
            userService.modifyAuthInfo(userAuthInfoVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }


    @GetMapping("/get/user/info/pages/{page}/{size}")
    @ApiOperation("后台查询所有用户信息接口")
    public ResultEntity<Page<User>> getUserPages(@PathVariable Long page,
                                                 @PathVariable Long size,
                                                 @RequestParam(value = "key_words", required = false) String keyWords,
                                                 @RequestParam(value = "authStatus", required = false) Integer authStatus) {
        try {
            Page<User> userPage = userService.getUserPages(page, size, keyWords, authStatus);
            return ResultEntity.success(userPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation("更新用户认证状态")
    @PutMapping("/modify/user/auth/Status/{uid}/{authStatus}")
    public ResultEntity<String> modifyUserAuthStatus(@PathVariable String uid,
                                                     @PathVariable Integer authStatus) {
        try {
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.eq("uid", uid);
            userUpdateWrapper.set("auth_status", authStatus);
            userService.update(userUpdateWrapper);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

}

