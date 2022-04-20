package com.wj.crowd.management.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.management.entity.Do.Admin;
import com.wj.crowd.management.entity.Vo.AdminVo;
import com.wj.crowd.management.mapper.AdminMapper;
import com.wj.crowd.management.service.api.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author w
 * @since 2022-04-10
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * 条件查询数据带分页
     * @param page          分页所需参数（当前页和每页数量）
     * @param keyWords 查询的条件
     * @return 管理员数据带分页
     */
    @Override
    public IPage<AdminVo> getAdminPages(Page<Admin> page, String keyWords) {
        // 获取条件

        // 封装查询条件
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.like(!ObjectUtils.isEmpty(keyWords), "login_acct", keyWords);
        adminQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "email", keyWords);
        adminQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "nick_name", keyWords);
        // 擦除超级管理员
        adminQueryWrapper.ne("id","1");

        // 查询结果进行超级管理员擦除并封装vo类
        List<AdminVo> adminList = new ArrayList<>();

        Page adminPages = baseMapper.selectPage(page, adminQueryWrapper);
        for (Admin admin : (List<Admin>) adminPages.getRecords()) {
            // 擦除超级管理员
//            if ("1".equals(admin.getId())) {
//                adminPages.setTotal(adminPages.getTotal()-1);
//                continue;
//            }
            AdminVo adminVo = new AdminVo();
            BeanUtils.copyProperties(admin, adminVo);
            adminVo.setPassword("");
            adminList.add(adminVo);
        }

        IPage<AdminVo> adminVoIPage = adminPages.setRecords(adminList);

        return adminVoIPage;
    }

    /**
     * 根据id获取管理员信息
     * @param id 管理员id
     * @return 管理员信息
     */
    @Override
    public AdminVo getAdminById(String id) {
        // 根据id查询对象
        Admin admin = baseMapper.selectById(id);

        if(null==admin){
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }
        // 封装vo对象
        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(admin, adminVo);
        adminVo.setPassword("");
        return adminVo;
    }

    /**
     * 修改管理员信息
     * @param id      管理员id
     * @param adminVo 管理员信息
     */
    @Override
    public void modifyAdminById(String id, AdminVo adminVo) {
        // 根据id查询修改对象
        Admin admin = baseMapper.selectById(id);

        if (null == admin) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        // 更新数据
        BeanUtils.copyProperties(adminVo, admin);

        // 手动更新更新时间
        admin.setUpdateTime(LocalDateTime.now());

        int updateResult = baseMapper.updateById(admin);

        if (updateResult <= 0) {
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }
    }

    /**
     *
     * 新增管理员
     * @param adminVo 管理员账号
     *
     */
    @Override
    public void saveAdmin(AdminVo adminVo) {
        // 判断登录账号是否已经被占用
        String loginAcct = adminVo.getLoginAcct();
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("login_acct", loginAcct);
        Long adminCount = baseMapper.selectCount(adminQueryWrapper);

        if (adminCount > 0) {
            throw new CrowdException(ResultCodeEnum.LOGIN_ACCT_IN_USED);
        }

        // 进行管理员账号保存
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminVo, admin);

        // 对密码进行带盐值加密
        String password = admin.getPassword();
        String encodePassword = bCryptPasswordEncoder.encode(password);

        admin.setPassword(encodePassword);

        int saveResult = baseMapper.insert(admin);
        if(saveResult<=0){
            throw new CrowdException(ResultCodeEnum.SAVE_DATA_ERROR);
        }
    }
}