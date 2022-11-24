package com.data.rabbitMqConfig;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


/**
 * 实现RabbitMQ的消费者有两种模式，推模式（Push）和拉模式（Pull）
 *
 * 由于某些限制，消费者在某个条件成立时才能消费消息
 *
 * 需要批量拉取消息进行处理
 *
 * 批量监听 MQ版本要在 2.2 以上
 * The type Rabbit config.
 */
@Configuration
public class RabbitConfig {
    /**
     * Json message converter message converter.消息转换器
     * @return the message converter
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Rabbit template rabbit template.
     * @return the rabbit template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    /**
     * Batch queue rabbit listener container factory simple rabbit listener container factory.
     * @param connectionFactory the connection factory
     * @return the simple rabbit listener container factory
     */
    @Bean("batchQueueRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory batchQueueRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        // 超过X秒后批量获取消息
        factory.setBatchingStrategy(new SimpleBatchingStrategy(100, 1024, 5000));
        factory.setBatchListener(true);
        factory.setBatchSize(10);
        // 消费端接收的响应时间
        factory.setReceiveTimeout(30000L);
        factory.setConsumerBatchEnabled(true);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConnectionFactory(connectionFactory);
        // 设置线程数
        factory.setConcurrentConsumers(10);
        // 最大线程数
        factory.setMaxConcurrentConsumers(20);
        return factory;
    }

    /**
     * Batch queue task scheduler task scheduler.
     * @return the task scheduler
     */
    @Bean("batchQueueTaskScheduler")
    public TaskScheduler batchQueueTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }


    /**
     * Batch queue rabbit template batching rabbit template. 注册批量处理rabbitTemplate
     *
     * 所谓批量.就是spring 将多条message重新组成一条message, 发送到mq, 从mq接受到这条message后,在重新解析成多条message
     * @param connectionFactory the connection factory
     * @param taskScheduler     the task scheduler
     * @return the batching rabbit template
     */
    @Bean("batchQueueRabbitTemplate")
    public BatchingRabbitTemplate batchQueueRabbitTemplate(ConnectionFactory connectionFactory,
                                                           @Qualifier("batchQueueTaskScheduler") TaskScheduler taskScheduler) {
        // 一次批量的数量
        int batchSize = 10;
        // 缓存大小限制,单位字节，
        // simpleBatchingStrategy的策略，是判断message数量是否超过batchSize限制或者message的大小是否超过缓存限制，
        // 缓存限制:主要用于限制"组装后的一条消息的大小"
        // 如果主要通过数量来做批量("打包"成一条消息), 缓存设置大点
        // 详细逻辑请看simpleBatchingStrategy#addToBatch()
        // 1 KB
        int bufferLimit = 1024;
        // 超时时间(ms)
        long timeout = 10000;
        // 注意，该策略只支持一个exchange/routingKey
        // A simple batching strategy that supports only one exchange/routingKey
        BatchingStrategy batchingStrategy = new SimpleBatchingStrategy(batchSize, bufferLimit, timeout);
        return new BatchingRabbitTemplate(connectionFactory, batchingStrategy, taskScheduler);
    }
}
