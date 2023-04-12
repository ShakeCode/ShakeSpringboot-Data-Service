/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.rabbitMqConfig.dead_letter_delay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用rabbitmq插件实现延迟消息： https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases
 *
 * 下载插件文件：rabbitmq_delayed_message_exchange-3.9.0.ez
 *
 * 下载后安装插件
 * # 查看容器ID
 * docker ps -a
 * #  将本地文件复制到docker容器中
 * docker cp /Users/lj/Downloads/rabbitmq_delayed_message_exchange-3.9.0.ez 1faca6a70742:/opt/rabbitmq/plugins
 * docker exec -it 1faca6a70742 /bin/bash
 * cd /opt/rabbitmq/sbin
 * ./rabbitmq-plugins enable rabbitmq_delayed_message_exchange
 * docker restart 1faca6a70742
 * <p>
 * ps:
 * 开启远程管理，否则通过15672无法登陆管理页面
 * 进入到docker容器中执行：
 * <p>
 * # 查看容器id
 * docker ps -a
 * # 进入容器
 * docker exec -it 容器id /bin/bssh
 * # 容器内执行
 * rabbitmq-plugins enable rabbitmq_management
 */
// @ConditionalOnExpression("${rabbit.deplay.plugin.enable:false}")
@ConditionalOnExpression("'${rabbit.deplay.plugin.enable:true}'.equals('true')")
// @ConditionalOnMissingClass("com.data.rabbitMqConfig.dead_letter_delay.RabbitMqDeadLetterConfig")
@Configuration
public class RabbitOrderDelayListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitOrderDelayListener.class);
    /**
     * The constant ORDER_EXCHANGE. 常规业务交换机
     */
    public static final String ORDER_EXCHANGE = "order-exchange";
    /**
     * The constant ORDER_KEY.  常规业务路由key
     */
    public static final String ORDER_KEY = "order-key";
    /**
     * The constant ORDER_QUEUE.常规业务队列
     */
    public static final String ORDER_QUEUE = "order-queue";

    /**
     * The constant DEAD_EXCHANGE. 死信交换机
     */
    public static final String DEAD_EXCHANGE = "dead-exchange";
    /**
     * The constant DEAD_KEY. 死信路由key, 按照业务选择交换机类型
     */
    public static final String DEAD_KEY = "dead-key";

    /**
     * The constant DEAD_ORDER_CANCEL_QUEUE.死信业务取消队列-监听死信队列后处理消息
     */
    public static final String DEAD_ORDER_CANCEL_QUEUE = "dead-order-cancel-queue";


    /**
     * Order queue org . springframework . amqp . core . queue.
     * @return the org . springframework . amqp . core . queue
     */
    @Bean
    public org.springframework.amqp.core.Queue orderQueue() {
        // 必须安装延迟队列插件,否则报错
        Map<String, Object> args = new HashMap<>(2);
        // 设置死信交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 死信队列的路由
        args.put("x-dead-letter-routing-key", DEAD_KEY);
        // 消息驻留队列过期时间(毫秒)后变为死信消息进入死信交换机
        args.put("x-message-ttl", 30 * 1000);
        // 普通业务队列绑定声明(添加队列属性包含死信交换机和死信路由key、过期消息)
        return new org.springframework.amqp.core.Queue(ORDER_QUEUE, true, false, false, args);
        // 等价如下一次性绑定交换机
        // return new CustomExchange(DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }

    /**
     * Order exchange direct exchange.交换机
     * @return the direct exchange
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }

    /**
     * Order binding binding.交换机路由绑定队列
     * @return the binding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(this.orderQueue()).to(this.orderExchange()).with(ORDER_KEY);
    }

    /**
     * Receive cancel order msg. 监听即将取消的订单信息（来自死信交换机）
     * @param message the message
     */
    @RabbitListener(
            ackMode = "AUTO",
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = DEAD_ORDER_CANCEL_QUEUE),
                            exchange = @Exchange(value = DEAD_EXCHANGE), key = DEAD_KEY
                    )
            })
    public void receiveCancelOrderMsg(String message) {
        LOGGER.info("收到过期订单消息:{}, 即将取消订单", message);
        // 业务处理... 取消下单等

    }
}
