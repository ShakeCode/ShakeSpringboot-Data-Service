/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.rabbitMqConfig.dead_letter_delay;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;

/**
 * The type Rabbit dead letter case config.
 *
 * <一>
 * 死信队列 实现12306中的30分钟后未支付订单取消。
 *
 * 【死信定义】：
 * 没有被成功消费的消息，但并不是所有未成功消费的消息都是死信消息，死信消息的产生来源于以下三种途径：
 * （1）消息被消费者拒绝，参数requeue设置为false的消息
 * （2）过期的消息，过期消息分为两种：
 *       a. 发送消息时，设置了某一条消息的生存时间（message TTL），如果生存时间到了，消息还没有被消费，就会被标注为死信消息
 *        b. 设置了队列的消息生存时间，针对队列中所有的消息，如果生存时间到了，消息还没有被消费，就会被标注为死信消息
 * （3）当队列达到了最大长度后，再发送过来的消息就会直接变成死信消息
 *
 * 【死信队列的作用】
 * （1）队列在已满的情况下，会将消息发送到死信队列中，这样消息就不会丢失了，回头再从死信队列里将消息取出来进行消费即可
 * （2）可以基于死信队列实现延迟消费的效果
 *
 * 延迟队列属于特殊死信队列
 *
 * 延迟发送思路：
 * （1）消息发送时设置消息的生存时间，其生存时间就是我们想要延迟的时间
 * （2）消息者监控死信队列进行消费
 *
 * 正常队列的消息因为没有消费者消费，同时又指定了生存时间，到达时间后消息转发到死信队列中，消费者监听了死信队列从而将其消费掉。
 *
 * 二、 使用rabbitmq插件实现： https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases
 */
@Configuration
public class RabbitDeadLetterCaseConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitDeadLetterCaseConfig.class);

    /**
     * The constant SHOP_ORDER_EXCHANGE.
     */
    public static final String SHOP_ORDER_EXCHANGE = "shop-order-exchange";
    /**
     * The constant SHOP_ORDER_KEY.
     */
    public static final String SHOP_ORDER_KEY = "shop-order-key";

    /**
     * The constant DEAD_SHOP_ORDER_EXCHANGE.
     */
    public static final String DEAD_SHOP_ORDER_EXCHANGE = "dead-shop-order-exchange";
    /**
     * The constant DEAD_SHOP_ORDER_KEY.
     */
    public static final String DEAD_SHOP_ORDER_KEY = "dead-shop-order-key";

    /**
     * Receive order. 死信队列场景
     * @param orderInfo the order info
     * @param message   the message
     * @param tag       the tag
     * @param channel   the channel
     * @throws IOException the io exception
     */
    @RabbitListener(
            ackMode = "MANUAL",
            bindings = {
                    // 订单队列 设置绑定死信交换机和消息ttl
                    @QueueBinding(value = @Queue(value = "shop-order-queue", arguments =
                            // 声明消息未消费后进入死信交换机
                            {@Argument(name = "x-dead-letter-exchange", value = DEAD_SHOP_ORDER_EXCHANGE),
                                    @Argument(name = "x-dead-letter-routing-key", value = DEAD_SHOP_ORDER_KEY),
                            }),
                            exchange = @Exchange(value = SHOP_ORDER_EXCHANGE, type = ExchangeTypes.DIRECT),
                            key = {SHOP_ORDER_KEY}
                    )
            })
    public void receiveOrder(String orderInfo, Message message, @Header(AmqpHeaders.DELIVERY_TAG) Long tag, Channel channel) throws IOException {
        try {
            LOGGER.info("收到下单消息:{}", orderInfo);
            long delivery = message.getMessageProperties().getDeliveryTag();
            // 异常下 拒绝握手 马上进入死信, 没有延时
            channel.basicReject(tag, false);
            // 不握手消息 未拒绝 未到死信
            // int i = 1 / 0;
            // 正常握手 非死信消息 不会再次进入死信队列
            //  channel.basicAck(tag, false);
        } catch (Exception e) {
            LOGGER.error("普通队列-消费者,监听到消息：{},发生异常,消息不再归入队列中,转向死信队列,异常e：", message, e);
            // channel.basicNack(tag, false, false);
            channel.basicReject(tag, false);
        }
    }


    /**
     * Receive.
     * @param message the message
     */
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "order-cancel-queue"),
                    exchange = @Exchange(value = DEAD_SHOP_ORDER_EXCHANGE), key = DEAD_SHOP_ORDER_KEY
            )
    })
    public void receive(String message) {
        LOGGER.info("死信消息已到达:{}, 即将取消订单", message);
    }
}
