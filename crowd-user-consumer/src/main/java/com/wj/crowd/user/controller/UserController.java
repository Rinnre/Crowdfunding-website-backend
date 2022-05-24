package com.wj.crowd.user.controller;

import com.wj.crowd.api.msm.MsmRemoteService;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.api.redis.CommonRedisRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.common.utils.JwtHelper;
import com.wj.crowd.entity.Do.User;
import com.wj.crowd.entity.Vo.user.LoginAndRegisteredUserVo;
import com.wj.crowd.entity.Vo.user.SimpleUserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 16:47
 */
@RestController
@ApiOperation("用户模块")
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @Autowired
    private CommonRedisRemoteService commonRedisRemoteService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MsmRemoteService msmRemoteService;


    @PostMapping("/do/login")
    @ApiOperation("密码登录登录")
    public ResultEntity<SimpleUserVo> doLogin(@RequestBody LoginAndRegisteredUserVo loginAndRegisteredUserVo) {
        // 前端数据验证
        String phone = loginAndRegisteredUserVo.getPhone();
        String password = loginAndRegisteredUserVo.getPassword();

        if (null == password || null == phone) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        // 查询用户信息
        ResultEntity<User> userByPhoneRemote = mysqlRemoteService.getUserByPhoneRemote(phone);
        if (!userByPhoneRemote.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_OR_PASSWORD_ERROR);
        }

        User user = userByPhoneRemote.getData();
        if (null == user) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_OR_PASSWORD_ERROR);
        }

        // 进行密码校验
        String mysqlPassword = user.getPassword();

        if (!bCryptPasswordEncoder.matches(password, mysqlPassword)) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_OR_PASSWORD_ERROR);
        }

        // 返回登录账号基本信息和token
        SimpleUserVo simpleUserVo = new SimpleUserVo();
        BeanUtils.copyProperties(user, simpleUserVo);

        String token = JwtHelper.createToken(user.getUid(), user.getNickName());

        simpleUserVo.setToken(token);

        return ResultEntity.success(simpleUserVo);
    }

    @PostMapping("/do/login_or_register")
    @ApiOperation("手机登录注册")
    public ResultEntity<SimpleUserVo> doRegistered(@RequestBody LoginAndRegisteredUserVo loginAndRegisteredUserVo
    ) {
        // 数据验证
        String phone = loginAndRegisteredUserVo.getPhone();
        String code = loginAndRegisteredUserVo.getCode();
        if (null == phone || null == code) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_DATA_ERROR);
        }

        // 校验验证码
        ResultEntity<String> redisCodeResult = commonRedisRemoteService.getRedisStringValueRemoteByKey(phone + CrowdConstant.REGISTER_AND_LOGIN_CODE);
        boolean success = redisCodeResult.isSuccess();
        if (!success) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String redisCode = redisCodeResult.getData();

        if (!Objects.equals(code, redisCode)) {
            return ResultEntity.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 判断是否需要进行注册操作
        ResultEntity<User> userByPhoneRemoteResult = mysqlRemoteService.getUserByPhoneRemote(phone);

        if (!userByPhoneRemoteResult.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        User user = userByPhoneRemoteResult.getData();

        // 第一次登录进行注册操作
        if (null == user) {
            user = new User();
            user.setPhone(phone);
            ResultEntity<String> stringResultEntity = mysqlRemoteService.saveUserRemote(user);
            if (!stringResultEntity.isSuccess()) {
                return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
            }
            user = mysqlRemoteService.getUserByPhoneRemote(phone).getData();
        }


        // 返回用户信息和JWT生成的token
        SimpleUserVo simpleUserVo = new SimpleUserVo();
        BeanUtils.copyProperties(user, simpleUserVo);

        String token = JwtHelper.createToken(user.getUid(), user.getNickName());

        simpleUserVo.setToken(token);

        // 删除使用过的验证码
        ResultEntity<String> stringResultEntity = commonRedisRemoteService.removeRedisKeyRemote(phone + CrowdConstant.REGISTER_AND_LOGIN_CODE);
        if (!stringResultEntity.isSuccess()) {
            throw new CrowdException(ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success(simpleUserVo);

    }

    @GetMapping("/send/code/{phone}/{type}")
    @ApiOperation("发送验证码")
    public ResultEntity<String> sendCode(@PathVariable String phone, @PathVariable String type) {

        if (Objects.equals(type, "login")) {
            ResultEntity<String> stringResultEntity = msmRemoteService.sendCodeRemote(phone, CrowdConstant.REGISTER_AND_LOGIN_CODE);
            if (!stringResultEntity.isSuccess()) {
                return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
            }
        }
        if (Objects.equals(type, "modify")) {
            ResultEntity<String> stringResultEntity = msmRemoteService.sendCodeRemote(phone, CrowdConstant.MODIFY_CODE);
            if (!stringResultEntity.isSuccess()) {
                return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
            }
        }

        return ResultEntity.success();
    }

    @PostMapping("/modify/password/{uid}/{old_password}/{new_password}")
    @ApiOperation("修改密码")
    public ResultEntity<String> modifyPassword(@PathVariable String uid,
                                               @PathVariable("old_password") String oldPassword,
                                               @PathVariable("new_password") String newPassword) {

        // 查询用户信息
        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(uid);
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        User user = userByUidRemote.getData();
        if (null == user) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        // 进行密码校验
        String password = user.getPassword();
        if (!bCryptPasswordEncoder.matches(oldPassword, password)) {
            return ResultEntity.fail(CrowdConstant.PASSWORD_ERROR);
        }

        // 重置密码
        newPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyUserRemote(user);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();

    }

    @GetMapping("/confirm/modify_code/{uid}/{phone}/{code}")
    @ApiOperation("验证修改操作验证码")
    public ResultEntity<String> confirmModifyCode(@PathVariable String uid,
                                                  @PathVariable String phone,
                                                  @PathVariable String code) {
        // 获取redis中存储的验证码
        ResultEntity<String> redisStringValueRemoteByKey = commonRedisRemoteService.getRedisStringValueRemoteByKey(phone + CrowdConstant.MODIFY_CODE);
        if (!redisStringValueRemoteByKey.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String modifyCode = redisStringValueRemoteByKey.getData();
        if (null == modifyCode) {
            return ResultEntity.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 进行验证码比对
        if (!Objects.equals(code, modifyCode)) {
            return ResultEntity.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 更新验证码比对结果并移除验证码
        ResultEntity<String> stringResultEntity = commonRedisRemoteService.setRedisKeyValueRemote(uid + CrowdConstant.MODIFY_CODE, CrowdConstant.SUCCESS);
        commonRedisRemoteService.removeRedisKeyRemote(phone + CrowdConstant.MODIFY_CODE);

        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();

    }

    @PostMapping("/modify/password/{uid}/{new_password}")
    @ApiOperation("通过手机验证码修改密码")
    public ResultEntity<String> modifyPassword(@PathVariable String uid,
                                               @PathVariable("new_password") String newPassword) {
        // 判断验证码验证是否通过
        ResultEntity<String> redisStringValueRemoteByKey = commonRedisRemoteService.getRedisStringValueRemoteByKey(uid + CrowdConstant.MODIFY_CODE);
        if (!redisStringValueRemoteByKey.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        String confirmResult = redisStringValueRemoteByKey.getData();
        if (!CrowdConstant.SUCCESS.equals(confirmResult)) {
            return ResultEntity.build(null, ResultCodeEnum.DATA_ERROR);
        }

        // 更新密码
        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(uid);
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        User user = userByUidRemote.getData();
        if (null == user) {
            return ResultEntity.build(null, ResultCodeEnum.DATA_ERROR);
        }
        // 密码加密
        newPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newPassword);

        ResultEntity<String> stringResultEntity = mysqlRemoteService.saveUserRemote(user);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();
    }


}
