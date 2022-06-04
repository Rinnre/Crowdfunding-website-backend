package com.wj.crowd.mysql.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.entity.Do.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import io.swagger.models.auth.In;

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

    Page<User> getUserPages(Long page, Long size, String keyWords, Integer authStatus);

}
