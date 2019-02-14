package com.data.rabbitMqConfig.RevMsg;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutRev {

    @RabbitHandler
    @RabbitListener(queues = "fanout.A")
    public void  proccess2(Message message){
        System.out.println("--------fanout.A 接收到消息:"+ new String(message.getBody()));
    }

    @RabbitHandler
    @RabbitListener(queues = "fanout.B")
    public void  proccess3(Message message){
        System.out.println("--------fanout.B 接收到消息:"+ new String(message.getBody()));
    }
}
