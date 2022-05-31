package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.entity.Do.ShippingAddress;
import com.wj.crowd.mysql.mapper.ShippingAddressMapper;
import com.wj.crowd.mysql.service.api.ShippingAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author w
 * @since 2022-05-26
 */
@Service
public class ShippingAddressServiceImpl extends ServiceImpl<ShippingAddressMapper, ShippingAddress> implements ShippingAddressService {

    /**
     *  查询
     * @param uid
     * @return
     */
    @Override
    public List<ShippingAddress> getShippingAddress(String uid) {
        QueryWrapper<ShippingAddress> shippingAddressQueryWrapper = new QueryWrapper<>();
        shippingAddressQueryWrapper.eq("uid",uid);
        return baseMapper.selectList(shippingAddressQueryWrapper);
    }

    /**
     *  修改
     * @param shippingAddressVo
     */
    @Override
    public void modifyShippingAddress(ShippingAddress shippingAddressVo) {
        String id = shippingAddressVo.getId();
        ShippingAddress shippingAddress = baseMapper.selectById(id);
        if(null==shippingAddress){
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }
        // 更新数据
        String consigneeName = shippingAddressVo.getConsigneeName();

        if(null!=consigneeName&& !consigneeName.equals("")){
            shippingAddress.setConsigneeName(consigneeName);
        }

        String consigneeAddress = shippingAddressVo.getConsigneeAddress();

        if(null!=consigneeAddress&& !consigneeAddress.equals("")){
            shippingAddress.setConsigneeAddress(consigneeAddress);
        }

        String consigneeEmail = shippingAddressVo.getConsigneeEmail();
        if(null!=consigneeEmail&& !consigneeEmail.equals("")){
            shippingAddress.setConsigneeEmail(consigneeEmail);
        }

        String consigneePhone = shippingAddressVo.getConsigneePhone();
        if(null!=consigneePhone&& !consigneePhone.equals("")){
            shippingAddress.setConsigneePhone(consigneePhone);
        }

        shippingAddress.setUpdateTime(LocalDateTime.now());

        int update = baseMapper.updateById(shippingAddress);
        if(update<=0){
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }

    }
}
