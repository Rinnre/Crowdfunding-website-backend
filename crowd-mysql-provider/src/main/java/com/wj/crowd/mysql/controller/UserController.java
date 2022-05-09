package com.wj.crowd.mysql.controller;


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
    public ResultEntity<String> modifyAuthInfoRemote(UserAuthInfoVo userAuthInfoVo) {
        try {
            userService.modifyAuthInfo(userAuthInfoVo);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

}

