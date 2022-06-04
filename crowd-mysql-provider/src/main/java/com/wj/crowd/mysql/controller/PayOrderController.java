package com.wj.crowd.mysql.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.PayOrder;
import com.wj.crowd.entity.Do.Reward;
import com.wj.crowd.entity.Vo.order.PayOrderVo;
import com.wj.crowd.mysql.service.api.PayOrderService;
import com.wj.crowd.mysql.service.api.RewardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author w
 * @since 2022-05-31
 */
@RestController
@RequestMapping("/mysql/pay_order")
public class PayOrderController {
    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RewardService rewardService;

    @GetMapping("/get/reward/remote/by/{rewardId}")
    @ApiOperation("获取订单回报信息")
    public ResultEntity<Reward> getRewardByIdRemote(@PathVariable String rewardId){
        try {
            Reward reward = payOrderService.getRewardByIdRemote(rewardId);
            if(null==reward){
                return  ResultEntity.fail();
            }
            return ResultEntity.success(reward);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation("新建订单")
    @PostMapping("/save/order/info")
    public ResultEntity<String> saveOrderInfo(@RequestBody PayOrder payOrder){
        try {
            boolean saveResult = payOrderService.saveOrder(payOrder);
            if(!saveResult){
                return ResultEntity.fail(ResultCodeEnum.SAVE_DATA_ERROR.getMessage());
            }
            return ResultEntity.success(payOrder.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @PostMapping("/modify/reward/info/remote")
    @ApiOperation("更新回报数量")
    public ResultEntity<String> modifyReward(@RequestBody Reward reward){
        try {
            boolean result = rewardService.updateById(reward);
            if(!result){
                return ResultEntity.fail("数据更新失败！");
            }
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/get/order/info/detail/remote/{orderId}")
    public ResultEntity<PayOrder> getOrderInfoDetailRemote(@PathVariable String orderId){
        try {
            PayOrder payOrder = payOrderService.getById(orderId);
            if(null== payOrder){
                return ResultEntity.fail();
            }
            return ResultEntity.success(payOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }

    }

    @ApiOperation("修改订单信息")
    @PostMapping("/modify/order")
    public ResultEntity<String> modifyOrderInfoDetailRemote(@RequestBody PayOrder payOrder){
        try {
            payOrderService.modifyOrderInfo(payOrder);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @ApiOperation("查询用户所有订单信息")
    @GetMapping("/get/user/order/info/{uid}")
    public ResultEntity<List<PayOrderVo>> getUserOrderInfo(@PathVariable String uid,@RequestParam(required = false) String orderStatus){
        try {
            List<PayOrderVo> payOrderVoList = payOrderService.getUserOrderInfo(uid,orderStatus);
            return ResultEntity.success(payOrderVoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }

    }

    // 查询所有订单信息
    @ApiOperation("查询所有订单信息")
    @GetMapping("/get/all/order/pages/{page}/{size}")
    public ResultEntity<Page<PayOrderVo>> getAllOrderPages(@PathVariable  Long page,
                                                         @PathVariable Long size,
                                                         @RequestParam(required = false) String keyWords,
                                                         @RequestParam(required = false) String orderStatus){
        try {

            Page<PayOrderVo> orderPages = payOrderService.getAllOrderPages(page,size,keyWords,orderStatus);
            return ResultEntity.success(orderPages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }

    }
}

