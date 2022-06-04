package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.entity.Do.User;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import com.wj.crowd.mysql.mapper.UserMapper;
import com.wj.crowd.mysql.service.api.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-05-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 根据手机号查询用户信息
     * 用于登录
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Override
    public User getUserByPhone(String phone) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone", phone);
        return baseMapper.selectOne(userQueryWrapper);
    }

    /**
     * 更新用户信息
     *
     * @param userVo
     */
    @Override
    public void modifyUser(User userVo) {
        String uid = userVo.getUid();
        User user = baseMapper.selectById(uid);

        if (null == user) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 更新昵称
        String nickName = userVo.getNickName();
        if (null != nickName) {
            user.setNickName(nickName);
        }
        // 更新头像
        String avatar = userVo.getAvatar();
        if (null != avatar) {
            user.setAvatar(avatar);
        }
        // 更新性别
        Integer gender = userVo.getGender();
        if (null != gender) {
            user.setGender(gender);
        }

        // 更新生日
        Date birthday = userVo.getBirthday();
        if (null != birthday) {
            user.setBirthday(birthday);
        }

        // 更新密码
        String password = userVo.getPassword();
        if(null!=password&& !Objects.equals("",password)){
            user.setPassword(password);
        }

        // 更新个人信息
        String biography = userVo.getBiography();
        if (null != biography) {
            user.setBiography(biography);
        }

        // 更新数据数据
        int updateResult = baseMapper.updateById(user);
        if (updateResult <= 0) {
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }


    }

    /**
     *  更新认证信息
     * @param userAuthInfoVo
     */
    @Override
    public void modifyAuthInfo(UserAuthInfoVo userAuthInfoVo) {
        String uid = userAuthInfoVo.getUid();
        User user = baseMapper.selectById(uid);

        if(null== user){
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 数据更新
        BeanUtils.copyProperties(userAuthInfoVo,user);

        // 更新认证状态为2：认证中
        user.setAuthStatus(2);

        int updateResult = baseMapper.updateById(user);

        if(updateResult<=0){
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }

    }

    /**
     * 用户注册
     * @param user
     */
    @Override
    public void saveUser(User user) {
        String phone = user.getPhone();
        // 设置昵称
        user.setNickName(phone);

        // 随机生成密码
        UUID uuid = UUID.randomUUID();
        String password = String.valueOf(uuid).replace("-","").substring(0,11);
        password = bCryptPasswordEncoder.encode(password);
        user.setPassword(password);

        int insertResult = baseMapper.insert(user);
        if(insertResult<=0){
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }
    }

    /**
     *  后台接口
     * @param page
     * @param size
     * @param keyWords
     * @return
     */
    @Override
    public Page<User> getUserPages(Long page, Long size, String keyWords,Integer authStatus) {
        // 封装查询条件
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select(User.class,item -> !item .getColumn().equals("password"));
        userQueryWrapper.like(!ObjectUtils.isEmpty(keyWords), "uid", keyWords);
        userQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "phone", keyWords);
        userQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "nick_name", keyWords);
        userQueryWrapper.eq(!ObjectUtils.isEmpty(authStatus), "auth_status", authStatus);

        Page<User> userPage = new Page<>(page, size);

        return baseMapper.selectPage(userPage, userQueryWrapper);
    }
}
