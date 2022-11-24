package com.data.rabbitMqConfig.batchpull;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * https://www.cnblogs.com/exmyth/p/13822234.html
 * The type Rabbit batch pull service.
 */
@Service
public class RabbitBatchPullService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitBatchPullService.class);

    /**
     * The Rabbit admin.
     */

    private final AmqpAdmin rabbitAdmin;

    /**
     * The Rabbit template.
     */

    private final RabbitTemplate rabbitTemplate;

    /**
     * The Message converter.
     */
    @Lazy
    @Autowired
    private MessageConverter messageConverter;

    private volatile MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();

    /**
     * Instantiates a new Rabbit batch pull service.
     * @param rabbitAdmin    the rabbit admin
     * @param rabbitTemplate the rabbit template
     */
    public RabbitBatchPullService(AmqpAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Gets count.
     * @param queueName the queue name
     * @return the count
     */
    public int getCount(String queueName) {
        Properties properties = rabbitAdmin.getQueueProperties(queueName);
        return (Integer) properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT);
    }

    /**
     * Process queue.
     * @param <T>       the type parameter
     * @param queueName the queue name
     * @param count     the count
     * @param clazz     the clazz
     * @param consumer  the consumer
     */
    public <T> void processQueue(String queueName, Integer count, Class<T> clazz, Consumer<T> consumer) {
        // 获取队列消息数量
        int reprocessCount = this.getCount(queueName);
        if (reprocessCount <= 0) {
            LOGGER.info("empty queue messge get!");
            return;
        }
        int requestCount = count != null ? count : reprocessCount;
        for (int i = 0; i < requestCount; i++) {
            rabbitTemplate.execute(channel -> {
                GetResponse response = channel.basicGet(queueName, false);
                T result = null;
                if (!Optional.ofNullable(response).isPresent()) {
                    return result;
                }
                try {
                    MessageProperties messageProps = messagePropertiesConverter.toMessageProperties(response.getProps(), response.getEnvelope(), StandardCharsets.UTF_8.name());
                    if (response.getMessageCount() >= 0) {
                        messageProps.setMessageCount(response.getMessageCount());
                    }
                    Message message = new Message(response.getBody(), messageProps);
                    result = (T) messageConverter.fromMessage(message);
                    consumer.accept(JSONObject.parseObject(new String(message.getBody(), StandardCharsets.UTF_8), clazz));
                    channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
                } catch (Exception exp) {
                    LOGGER.error("pull processQueue, error", exp);
                    channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);
                }
                return result;
            });
        }
    }
}
