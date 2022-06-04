package com.wj.crowd.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.entity.Do.PayOrder;
import com.wj.crowd.entity.Do.Picture;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Do.Reward;
import com.wj.crowd.entity.Vo.address.ShippingAddressVo;
import com.wj.crowd.entity.Vo.order.PayOrderVo;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import com.wj.crowd.entity.Vo.project.RewardVo;
import com.wj.crowd.mysql.mapper.PayOrderMapper;
import com.wj.crowd.mysql.service.api.PayOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wj.crowd.mysql.service.api.PictureService;
import com.wj.crowd.mysql.service.api.ProjectService;
import com.wj.crowd.mysql.service.api.RewardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ProjectService projectService;


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
        if (null != payOrderVo.getPayTime() && payOrder.getPayTime() == null) {
            payOrder.setPayTime(payOrderVo.getPayTime());
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
            if (!b) {
                return false;
            }
        }
        return insert > 0;
    }

    @Override
    public List<PayOrderVo> getUserOrderInfo(String uid, String orderStatus) {
        QueryWrapper<PayOrder> payOrderQueryWrapper = new QueryWrapper<>();
        payOrderQueryWrapper.eq("uid", uid);
        payOrderQueryWrapper.eq(null != orderStatus, "order_status", orderStatus);
        List<PayOrder> payOrders = baseMapper.selectList(payOrderQueryWrapper);

        List<PayOrderVo> payOrderVos = new ArrayList<>();

        // 遍历order对象查询其回报信息和封装为vo对象

        payOrders.forEach(payOrder -> {

            PayOrderVo payOrderVo = new PayOrderVo();
            BeanUtils.copyProperties(payOrder, payOrderVo);

            // 收货人地址信息转换
            ShippingAddressVo address = new ShippingAddressVo();
            address.setConsigneeName(payOrder.getConsigneeName());
            address.setConsigneePhone(payOrder.getConsigneePhone());
            address.setConsigneeEmail(payOrder.getConsigneeEmail());
            address.setConsigneeAddress(payOrder.getConsigneeAddress());

            payOrderVo.setAddress(address);

            // 获取项目名称
            Project project = projectService.getById(payOrder.getProjectId());

            payOrderVo.setProjectName(project.getTitle());

            // 回报信息转换
            String rewardId = payOrder.getRewardId();
            Reward reward = rewardService.getById(rewardId);
            QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
            pictureQueryWrapper.eq("foreign_id", rewardId);
            pictureQueryWrapper.eq("type", CrowdConstant.PICTURE_TYPE_REWARD);
            List<Picture> pictureList = pictureService.getBaseMapper().selectList(pictureQueryWrapper);
            List<PictureVo> pictureVos = new ArrayList<>();

            pictureList.forEach(picture -> {
                PictureVo pictureVo = new PictureVo();
                BeanUtils.copyProperties(picture, pictureVo);
                pictureVos.add(pictureVo);
            });


            RewardVo rewardVo = new RewardVo();
            BeanUtils.copyProperties(reward, rewardVo);
            rewardVo.setPictureVos(pictureVos);

            payOrderVo.setRewardVo(rewardVo);
            payOrderVos.add(payOrderVo);
        });
        return payOrderVos;
    }

    /**
     * 后台查询所有订单
     *
     * @param page
     * @param size
     * @param keyWords
     * @param orderStatus
     * @return
     */
    @Override
    public Page<PayOrderVo> getAllOrderPages(Long page, Long size, String keyWords, String orderStatus) {
        Page<PayOrder> orderPage = new Page<>(page, size);
        QueryWrapper<PayOrder> payOrderQueryWrapper = new QueryWrapper<>();
        payOrderQueryWrapper.like(!ObjectUtils.isEmpty(keyWords), "id", keyWords);
        payOrderQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "project_id", keyWords);
        payOrderQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "uid", keyWords);
        payOrderQueryWrapper.or().like(!ObjectUtils.isEmpty(keyWords), "consignee_name", keyWords);

        payOrderQueryWrapper.eq(!ObjectUtils.isEmpty(orderStatus), "order_status", orderStatus);

        Page<PayOrderVo> payOrderVoPage = new Page<>(page, size);
        Page<PayOrder> orderPages = baseMapper.selectPage(orderPage, payOrderQueryWrapper);
        if(orderPages.getRecords()!=null){

            List<PayOrder> records = orderPages.getRecords();
            List<PayOrderVo> payOrderVos = new ArrayList<>();
            records.forEach(record -> {
                PayOrderVo payOrderVo = new PayOrderVo();
                BeanUtils.copyProperties(record, payOrderVo);

                // 项目信息
                String projectId = record.getProjectId();
                Project project = projectService.getById(projectId);
                payOrderVo.setProjectName(project.getName());

                // 回报信息
                String rewardId = record.getRewardId();
                Reward reward = rewardService.getById(rewardId);
                RewardVo rewardVo = new RewardVo();

                QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
                pictureQueryWrapper.eq("foreign_id", rewardId);
                List<Picture> pictureList = pictureService.getBaseMapper().selectList(pictureQueryWrapper);
                if (null != pictureList && pictureList.size() != 0) {
                    List<PictureVo> pictureVos = new ArrayList<>();
                    pictureList.forEach(picture -> {
                        PictureVo pictureVo = new PictureVo();
                        BeanUtils.copyProperties(picture, pictureVo);
                        pictureVos.add(pictureVo);
                    });
                    rewardVo.setPictureVos(pictureVos);
                }
                BeanUtils.copyProperties(reward, rewardVo);

                payOrderVo.setRewardVo(rewardVo);
                payOrderVos.add(payOrderVo);

            });
        payOrderVoPage.setRecords(payOrderVos);
        }

        payOrderVoPage.setPages(orderPages.getPages());
        payOrderVoPage.setTotal(orderPage.getTotal());

        return payOrderVoPage;
    }
}
