package com.wj.crowd.mysql.service.api;

import com.wj.crowd.entity.Do.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import com.wj.crowd.entity.Vo.user.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-05-06
 */
public interface UserService extends IService<User> {

    User getUserByPhone(String phone);

    void modifyUser(User user);

    void modifyAuthInfo(UserAuthInfoVo userAuthInfoVo);

    void saveUser(User user);
}
