package com.data.rabbitMqConfig.RevMsg;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 路由模式
 * 消息持久化：不会服务宕机而出现消息丢失，必须交换机，队列，消息属性都是duable=true, autodelete = false， 再加上事务确认confirm机制,消息可设置存活期
 */
@Component
public class DirectRev {

    @RabbitHandler
    @RabbitListener(queues = "queueOneDir")
    public void proccess1(Message message, Channel channel) throws IOException {
        long tag = message.getMessageProperties().getDeliveryTag();
        try {
            System.out.println("--------queueOneDir 接收到消息:" + new String(message.getBody()));
            channel.basicAck(tag,false);
        } catch (IOException e) {
            e.printStackTrace();
            channel.basicReject(tag,false);
        }
    }

    @RabbitHandler
    @RabbitListener(queues = "queueTwoDir")
    public void proccess2(Message message) {
        System.out.println("--------queueTwoDir 接收到消息:" + new String(message.getBody()));
    }
}
