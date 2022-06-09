package com.data.rabbitMqConfig.RevMsg;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicRev {

    @RabbitHandler
    @RabbitListener(queues = "queueOne")
    public void  proccess(Message message){
        System.out.println("--------queueOne 接收到消息:"+ new String(message.getBody()));
    }

    @RabbitHandler
    @RabbitListener(queues = "queueTwo")
    public void  proccess1(Message message){
        System.out.println(message.getMessageProperties());
        System.out.println("--------queueTwo 接收到消息:"+ new String(message.getBody()));
    }

    @RabbitHandler
    @RabbitListener(queues = "queueThree")
    public void  proccess2(Message message){
        System.out.println("--------queueThree接收到消息:"+ new String(message.getBody()));
    }

}
