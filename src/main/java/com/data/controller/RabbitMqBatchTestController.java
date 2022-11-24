package com.data.controller;

import com.data.model.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Rabbit mq batch test controller.
 */
@RequestMapping("/v1/rabbit")
@RestController
@Api(value = "rabbitmq批量测试", tags = "rabbitmq批量测试")
public class RabbitMqBatchTestController {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqBatchTestController.class);

    private final BatchingRabbitTemplate batchQueueRabbitTemplate;


    /**
     * Instantiates a new Rabbit mq batch test controller.
     * @param batchQueueRabbitTemplate the batch queue rabbit template
     */
    public RabbitMqBatchTestController(BatchingRabbitTemplate batchQueueRabbitTemplate) {
        this.batchQueueRabbitTemplate = batchQueueRabbitTemplate;
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
}
