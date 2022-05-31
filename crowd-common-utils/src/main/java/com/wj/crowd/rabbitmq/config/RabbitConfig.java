package com.wj.crowd.rabbitmq.config;

import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.rabbitmq.constant.RabbitConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wj
 * @descript
 * @date 2022/5/30 - 21:01
 */
@Configuration
public class RabbitConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 死信接收队列
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(RabbitConstant.ORDER_QUEUE_NAME, true);
    }

    /**
     * 死信交换机
     * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。
     * 符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”。
     **/
    @Bean
    public DirectExchange orderDirectExchange() {
        return new DirectExchange(RabbitConstant.ORDER_EXCHANGE_NAME);
    }

    /**
     * 死信接收队列绑定交换机
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
                .bind(orderQueue())
                .to(orderDirectExchange())
                .with(RabbitConstant.ORDER_ROUTING_KEY);
    }


    /**
     * 延迟队列配置(死信队列)
     * <p>
     * 1、params.put("x-message-ttl", 5 * 1000);
     * 第一种方式是直接设置 Queue 延迟时间 但如果直接给队列设置过期时间,这种做法不是很灵活,（当然二者是兼容的,默认是时间小的优先）
     * 2、rabbitTemplate.convertAndSend(book, message -> {
     * message.getMessageProperties().setExpiration(2 * 1000 + "");
     * return message;
     * });
     * 第二种就是每次发送消息动态设置延迟时间,这样我们可以灵活控制
     **/
    @Bean
    public Queue delayOrderQueue() {
        Map<String, Object> params = new HashMap<>();
        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
        params.put("x-dead-letter-exchange", RabbitConstant.ORDER_EXCHANGE_NAME);
        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
        params.put("x-dead-letter-routing-key", RabbitConstant.ORDER_ROUTING_KEY);
        params.put("x-message-ttl", RabbitConstant.ORDER_TTL);
        return new Queue(RabbitConstant.ORDER_DELAY_QUEUE, true, false, false, params);
    }

    /**
     * 需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange(RabbitConstant.ORDER_DELAY_EXCHANGE);
    }

    /**
     * 延迟队列绑定交换机
     */
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder
                .bind(delayOrderQueue())
                .to(orderDelayExchange())
                .with(RabbitConstant.ORDER_DELAY_ROUTING_KEY);
    }


}
