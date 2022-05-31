package com.wj.crowd.order.receiver;

import com.rabbitmq.client.Channel;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.PayOrder;
import com.wj.crowd.rabbitmq.constant.RabbitConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author wj
 * @descript
 * @date 2022/4/8 - 13:53
 */
@Component
@Slf4j
public class OrderReceiver {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @RabbitListener(queues = RabbitConstant.ORDER_QUEUE_NAME)
    public void orderDelayQueue(Message message, Channel channel) throws IOException {
        String orderId = new String(message.getBody()).replace("\"", "");
//        log.info("orderDelayQueue:"+new String( message.getBody()));
//        log.info(new Date().toString());
        // 查找数据库订单状态
        log.info(orderId);
        ResultEntity<PayOrder> orderInfoDetailRemote = mysqlRemoteService.getOrderInfoDetailRemote(orderId);
        if(!orderInfoDetailRemote.isSuccess()){
            log.info("查询订单状态失败！");
            return;
        }
        // 未支付订单设置为取消订单
        PayOrder payOrder = orderInfoDetailRemote.getData();

        if(Objects.equals(payOrder.getOrderStatus(), CrowdConstant.ORDER_STATUS_UNPAID)){
            payOrder.setOrderStatus(CrowdConstant.ORDER_STATUS_CANCEL);
        }

        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyOrderInfoDetailRemote(payOrder);
        if(!stringResultEntity.isSuccess()){
            log.info("更新订单状态失败！");
            return;
        }
        log.info("订单状态已修改");


    }
}
