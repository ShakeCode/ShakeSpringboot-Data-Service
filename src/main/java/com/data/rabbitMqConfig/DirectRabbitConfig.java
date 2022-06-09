package com.data.rabbitMqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由模式，路由key必须完全匹配
 */
@Configuration
public class DirectRabbitConfig {
    final static String queueOneDir = "queueOneDir";
    final static String queueTwoDir = "queueTwoDir";

    @Bean(name = "queueOneDir")
    public Queue queueOneDir() {
        return new Queue(DirectRabbitConfig.queueOneDir);
    }

    @Bean(name = "queueTwoDir")
    public Queue queueTwoDir() {
        return new Queue(DirectRabbitConfig.queueTwoDir);
    }

    @Bean("directExchange")
    DirectExchange directExchange() {
        return new DirectExchange("MyDirectExchange");
    }

    @Bean
    Binding bindingExchangeMessage(@Qualifier("queueOneDir") Queue queueMessage, @Qualifier("directExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("direct");
    }

    @Bean
    Binding bindingExchangeMessage1(@Qualifier("queueTwoDir") Queue queueMessage, @Qualifier("directExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("direct-two");
    }
}
