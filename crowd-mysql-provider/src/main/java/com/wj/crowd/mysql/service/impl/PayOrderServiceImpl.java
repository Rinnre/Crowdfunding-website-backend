package com.wj.crowd.mysql.service.impl;

import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.entity.Do.PayOrder;
import com.wj.crowd.entity.Do.Reward;
import com.wj.crowd.mysql.mapper.PayOrderMapper;
import com.wj.crowd.mysql.service.api.PayOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.mysql.service.api.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author w
 * @since 2022-05-31
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    @Autowired
    private RewardService rewardService;

    @Override
    public void modifyOrderInfo(PayOrder payOrderVo) {
        String id = payOrderVo.getId();
        PayOrder payOrder = baseMapper.selectById(id);
        if (null == payOrder) {
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }
        // 更新订单状态
        if (null != payOrderVo.getOrderStatus() && !Objects.equals(payOrder.getOrderStatus(), payOrderVo.getOrderStatus())) {
            payOrder.setOrderStatus(payOrderVo.getOrderStatus());
        }
        // 更新支付流水号
        if (null != payOrderVo.getPayNum() && payOrder.getPayNum() == null) {
            payOrder.setPayNum(payOrderVo.getPayNum());
        }
        if (!Objects.equals(payOrder.getOrderStatus(), CrowdConstant.ORDER_STATUS_PENDING_SHIPMENT)) {
            // 更新订单信息
            if (null != payOrderVo.getConsigneeName() && !Objects.equals(payOrder.getConsigneeName(), payOrderVo.getConsigneeName())) {
                payOrder.setConsigneeName(payOrderVo.getConsigneeName());
            }

            if (null != payOrderVo.getConsigneePhone() && !Objects.equals(payOrderVo.getConsigneePhone(), payOrder.getConsigneePhone())) {
                payOrder.setConsigneePhone(payOrderVo.getConsigneePhone());
            }

            if (null != payOrderVo.getConsigneeEmail() && !Objects.equals(payOrder.getConsigneeEmail(), payOrderVo.getConsigneeEmail())) {
                payOrder.setConsigneeEmail(payOrderVo.getConsigneeEmail());
            }

            if (null != payOrderVo.getConsigneeAddress() && !Objects.equals(payOrderVo.getConsigneeAddress(), payOrder.getConsigneeAddress())) {
                payOrder.setConsigneeAddress(payOrderVo.getConsigneeAddress());
            }
        }


        int updateResult = baseMapper.updateById(payOrder);
        if (updateResult <= 0) {
            throw new CrowdException(ResultCodeEnum.UPDATE_DATA_ERROR);
        }
    }

    @Override
    public Reward getRewardByIdRemote(String rewardId) {
        return rewardService.getById(rewardId);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean saveOrder(PayOrder payOrder) {
        // 下单
        int insert = baseMapper.insert(payOrder);
        // 更新回报数量
        Reward reward = rewardService.getById(payOrder.getRewardId());
        if (null == reward) {
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }
        if (reward.getLimitNumber() != -1) {
            Integer inventoryNumber = reward.getInventoryNumber();
            if (inventoryNumber < payOrder.getRewardCount()) {
                throw new CrowdException(ResultCodeEnum.DATA_ERROR);
            }

            reward.setInventoryNumber(Math.toIntExact(inventoryNumber - payOrder.getRewardCount()));

            boolean b = rewardService.updateById(reward);
            if(!b){
                return false;
            }
        }
        return insert > 0;
    }
}
