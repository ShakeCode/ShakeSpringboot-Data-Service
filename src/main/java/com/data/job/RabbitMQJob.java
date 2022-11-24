/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.job;

import com.alibaba.fastjson.JSON;
import com.data.rabbitMqConfig.batchpull.RabbitBatchPullService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * The type Rabbit mq job.
 */
@Component
public class RabbitMQJob {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQJob.class);

    private final RabbitBatchPullService rabbitBatchPullService;

    private final static String PULL_QUEUE = "pull-queue";


    /**
     * Instantiates a new Rabbit mq job.
     * @param rabbitBatchPullService the rabbit batch pull service
     */
    public RabbitMQJob(RabbitBatchPullService rabbitBatchPullService) {
        this.rabbitBatchPullService = rabbitBatchPullService;
    }

    /**
     * Pull queue queue.
     * @return the queue
     */
    @Bean(name = "pullQueue")
    public Queue pullQueue() {
        return new Queue(RabbitMQJob.PULL_QUEUE);
    }

    /**
     * Pull mq msg.
     */
    @Scheduled(cron = "${sync.mq.cron:0/10 * * * * ?}")
    public void pullMqMsg() {
        LOG.info("pull mq msg data start ...");
        rabbitBatchPullService.processQueue(PULL_QUEUE, 100, Object.class, new Consumer<Object>() {
            @Override
            public void accept(Object object) {
                LOG.info("pull mq msg, consumer msg: {}", JSON.toJSONString(object));
            }
        });
    }
}
