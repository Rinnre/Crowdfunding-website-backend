package com.wj.crowd.mysql.service.api;

import com.wj.crowd.entity.Do.ShippingAddress;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-05-26
 */
public interface ShippingAddressService extends IService<ShippingAddress> {

    List<ShippingAddress> getShippingAddress(String uid);

    void modifyShippingAddress(ShippingAddress shippingAddress);
}
