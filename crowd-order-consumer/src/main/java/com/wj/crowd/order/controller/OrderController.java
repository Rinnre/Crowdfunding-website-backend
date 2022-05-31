package com.wj.crowd.order.controller;

import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.common.utils.JwtHelper;
import com.wj.crowd.entity.Do.PayOrder;
import com.wj.crowd.entity.Do.Reward;
import com.wj.crowd.entity.Do.ShippingAddress;
import com.wj.crowd.entity.Vo.order.PayOrderVo;
import com.wj.crowd.rabbitmq.constant.RabbitConstant;
import com.wj.crowd.rabbitmq.server.RabbitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @descript
 * @date 2022/5/30 - 13:18
 */
@RestController
@RequestMapping("/order")
@Api("订单模块")
@Slf4j
public class OrderController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @Autowired
    private RabbitService rabbitService;

    // 确认订单信息
    @ApiOperation("确认订单信息")
    @GetMapping("/get/order/confirm/info/{reward_id}")
    public ResultEntity<Map<String,Object>> getOrderConfirmInfo(HttpServletRequest request,
                                                                @PathVariable("reward_id") String rewardId){
        Map<String,Object> map = new HashMap<>();

        // 查询用户地址
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        ResultEntity<List<ShippingAddress>> shippingAddress = mysqlRemoteService.getShippingAddress(userId);
        if(!shippingAddress.isSuccess()){
            return ResultEntity.fail();
        }

        map.put("userAddress",shippingAddress.getData());

        ResultEntity<Reward> rewardByIdRemote = mysqlRemoteService.getRewardByIdRemote(rewardId);
        if(!rewardByIdRemote.isSuccess()){
            return ResultEntity.fail();
        }


        map.put("reward",rewardByIdRemote.getData());
        return ResultEntity.success(map);

    }

    // 下单
    @ApiOperation("保存订单信息")
    @PostMapping("/save/order/info")
    public ResultEntity<String> saveOrderInfo(@RequestBody PayOrderVo payOrderVo){
        // 数据校验
        if(null== payOrderVo){
            return ResultEntity.fail("订单数据异常！");

        }

        PayOrder payOrder = new PayOrder();
        BeanUtils.copyProperties(payOrderVo, payOrder);
        // 设置收货信息和回报id
        payOrder.setConsigneeName(payOrderVo.getAddress().getConsigneeName());
        payOrder.setConsigneePhone(payOrderVo.getAddress().getConsigneePhone());
        payOrder.setConsigneeEmail(payOrderVo.getAddress().getConsigneeEmail());
        payOrder.setConsigneeAddress(payOrderVo.getAddress().getConsigneeAddress());
        payOrder.setRewardId(payOrderVo.getRewardVo().getId());
        // 设置订单状态为未支付
        payOrder.setOrderStatus(CrowdConstant.ORDER_STATUS_UNPAID);
        ResultEntity<String> stringResultEntity = mysqlRemoteService.saveOrderInfo(payOrder);

        if(!stringResultEntity.isSuccess()){
            return ResultEntity.build(stringResultEntity.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        String orderId = stringResultEntity.getData();

        if(null==orderId){
            return ResultEntity.build(null,ResultCodeEnum.NETWORK_ERROR);
        }
        // rabbitmq死信队列实现订单超时自动取消
//        log.info(new Date().toString());
        rabbitService.sendMessage(RabbitConstant.ORDER_DELAY_EXCHANGE,RabbitConstant.ORDER_DELAY_ROUTING_KEY,orderId);
        return ResultEntity.success(orderId);
    }

}
