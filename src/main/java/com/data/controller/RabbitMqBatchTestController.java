package com.data.controller;

import com.data.model.ResultVO;
import com.data.rabbitMqConfig.dead_letter_delay.OrderInfo;
import com.data.rabbitMqConfig.dead_letter_delay.RabbitDeadLetterCaseConfig;
import com.data.rabbitMqConfig.dead_letter_delay.RabbitOrderDelayListener;
import com.data.util.GsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * The type Rabbit mq batch test controller.
 */
@RequestMapping("/v1/rabbit")
@RestController
@Api(value = "rabbitmq批量测试", tags = "rabbitmq批量测试")
public class RabbitMqBatchTestController {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqBatchTestController.class);

    private final BatchingRabbitTemplate batchQueueRabbitTemplate;

    private final RabbitTemplate rabbitTemplate;

    /**
     * Instantiates a new Rabbit mq batch test controller.
     * @param batchQueueRabbitTemplate the batch queue rabbit template
     * @param rabbitTemplate           the rabbit template
     */
    public RabbitMqBatchTestController(BatchingRabbitTemplate batchQueueRabbitTemplate, RabbitTemplate rabbitTemplate) {
        this.batchQueueRabbitTemplate = batchQueueRabbitTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Batch send.
     * @return the result vo
     */
    @ApiOperation("批量发送MQ消息")
    @GetMapping("/batch/send")
    public ResultVO<String> batchSend() {
        // 除了send(String exchange, String routingKey, Message message, CorrelationData correlationData)方法是发送单条数据
        // 其他send都是批量发送
        //批量发送
        long timestamp = System.currentTimeMillis();
        String msg;
        Message message;
        MessageProperties messageProperties = new MessageProperties();
        for (int i = 0; i < 1000; i++) {
            msg = "batch." + timestamp + "-" + i;
            message = new Message(msg.getBytes(), messageProperties);
            batchQueueRabbitTemplate.send("batch-queue", message);
            // defaultRabbitTemplate.convertAndSend(RabbitMqConfig2.BATCH_QUEUE_NAME, msg.getBytes());
        }
        logger.info("发送数据完毕");
        logger.info("等待30s");
        return ResultVO.success();
    }


    /**
     * Send test queue with expiration result vo.
     * @return the result vo
     */
    @ApiOperation("商品下单-延迟X分钟取消")
    @GetMapping("/delay/order/msg")
    public ResultVO<String> sendTestQueueWithExpiration(@RequestParam(value = "delayMinute", defaultValue = "1") Integer delayMinute) {
        logger.info("用户开始预下单商品...");
        OrderInfo orderInfo = OrderInfo.builder().orderId(14345497941231649L).orderType("优惠下单").orderMsg("已下单，注意出货商品").createDate(new Date()).build();
        // 发送延迟订单取消消息
        rabbitTemplate.convertAndSend(RabbitOrderDelayListener.ORDER_EXCHANGE, RabbitOrderDelayListener.ORDER_KEY, GsonUtil.toJson(orderInfo), msg -> {
            // 过期时间X秒（优先级按照死信队列属性ttl为先） 变为死信消息 进入死信交换机
            msg.getMessageProperties().setExpiration(String.valueOf(delayMinute * 60 * 1000));
            return msg;
        });

        // 发送订单消息
        rabbitTemplate.convertAndSend(RabbitDeadLetterCaseConfig.SHOP_ORDER_EXCHANGE, RabbitDeadLetterCaseConfig.SHOP_ORDER_KEY, GsonUtil.toJson(orderInfo));
        return ResultVO.success("下单成功");
    }

}
