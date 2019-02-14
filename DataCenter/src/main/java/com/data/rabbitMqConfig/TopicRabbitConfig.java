package com.data.rabbitMqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {

    final static String queueOne = "queueOne";
    final static String queueTwo = "queueTwo";
    final static String queueThree = "queueThree";

    @Bean(name="queueOne")
    public Queue queueOne() {
        return new Queue(TopicRabbitConfig.queueOne);
    }

    @Bean(name="queueTwo")
    public Queue queueTwo() {
        return new Queue(TopicRabbitConfig.queueTwo);
    }

    @Bean(name="queueThree")
    public Queue queueThree() {
        return new Queue(TopicRabbitConfig.queueThree);
    }

    @Bean("topicExchange")
    TopicExchange topicExchange() {
        return new TopicExchange("MyTopicExchange");
    }

    @Bean
    Binding bindingExchangeMessage(@Qualifier("queueOne") Queue queueMessage, @Qualifier("topicExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.one");
    }

    @Bean
    Binding bindingQueueThreeToExchange(@Qualifier("queueTwo")Queue queueMessages,@Qualifier("topicExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.two");
    }

    @Bean
    Binding bindingQueueThreeToExchangeMessages(@Qualifier("queueThree")Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }
}
