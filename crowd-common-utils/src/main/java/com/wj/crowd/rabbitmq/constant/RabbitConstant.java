package com.wj.crowd.rabbitmq.constant;

import io.swagger.models.auth.In;

/**
 * @author wj
 * @descript
 * @date 2022/5/30 - 21:02
 */
public class RabbitConstant {
    /**
     * 延迟队列 TTL 名称
     */
    public static final String ORDER_DELAY_QUEUE = "user.order.delay.queue";

    /**
     * DLX，dead letter发送到的 exchange
     * 延时消息就是发送到该交换机的
     */
    public static final String ORDER_DELAY_EXCHANGE = "user.order.delay.exchange";

    /**
     * routing key 名称 路由键
     * 具体消息发送在该 routingKey 的
     */
    public static final String ORDER_DELAY_ROUTING_KEY = "order_delay";


    // 直接交换机定义
    public static final String ORDER_QUEUE_NAME = "user.order.queue";
    public static final String ORDER_EXCHANGE_NAME = "user.order.exchange";
    public static final String ORDER_ROUTING_KEY = "order";

    public static final Integer ORDER_TTL = 30000;
}
