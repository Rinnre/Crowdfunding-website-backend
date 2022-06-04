package com.wj.crowd.management.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wj
 * @descript
 * @date 2022/6/3 - 17:42
 */
@RestController
@RequestMapping("/management/user")
@CrossOrigin
public class UserController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;


    @ApiOperation("分页显示所有管理员信息")
    @GetMapping("/get/user/info/pages/{page}/{size}")
    public ResultEntity<Page<User>> getUserPages(@PathVariable Long page,
                                                 @PathVariable Long size,
                                                 @RequestParam(value = "key_words", required = false) String keyWords,
                                                 @RequestParam(value = "authStatus", required = false) Integer authStatus) {
        ResultEntity<Page<User>> userPages = mysqlRemoteService.getUserPages(page, size, keyWords, authStatus);
        return userPages;
    }

    @ApiOperation("更新用户认证状态")
    @PutMapping("/modify/user/auth/status/{uid}/{authStatus}")
    public ResultEntity<String> modifyUserAuthStatus(@PathVariable String uid,
                                                     @PathVariable Integer authStatus) {
        return mysqlRemoteService.modifyUserAuthStatus(uid, authStatus);
    }
}
