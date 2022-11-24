package com.data.rabbitMqConfig.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BatchMessageListener;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The type Batch queue listener.
 */
@Configuration
public class BatchQueueListener implements BatchMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchQueueListener.class);

    /**
     * On message batch. 批量接收处理
     * @param messages the messages
     */
    @RabbitListener(ackMode = "AUTO",
            concurrency = "3",
            queues = "batch-queue",
            containerFactory = "batchQueueRabbitListenerContainerFactory")
    @Override
    public void onMessageBatch(List<Message> messages) {
        LOGGER.info("batch.queue.consumer 收到{}条message", messages.size());
        if (messages.size() > 0) {
            LOGGER.info("第一条数据是: {}", new String(messages.get(0).getBody()));
        }
    }
}