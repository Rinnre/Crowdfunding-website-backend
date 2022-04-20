package com.wj.crowd.management.service.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.management.entity.Do.Admin;
import com.wj.crowd.management.entity.Vo.AdminSearchVo;
import com.wj.crowd.management.entity.Vo.AdminVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-04-10
 */
public interface AdminService extends IService<Admin> {

    IPage<AdminVo> getAdminPages(Page<Admin> adminPage, String keyWords);

    AdminVo getAdminById(String id);

    void modifyAdminById(String id, AdminVo adminVo);

    void saveAdmin(AdminVo adminVo);
}
