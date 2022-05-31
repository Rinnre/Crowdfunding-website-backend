package com.wj.crowd.mysql.service.api;

import com.wj.crowd.entity.Do.PayOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wj.crowd.entity.Do.Reward;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author w
 * @since 2022-05-31
 */
public interface PayOrderService extends IService<PayOrder> {

    void modifyOrderInfo(PayOrder payOrder);

    Reward getRewardByIdRemote(String rewardId);

    boolean saveOrder(PayOrder payOrder);
}
