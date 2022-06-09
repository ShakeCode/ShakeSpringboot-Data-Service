package com.data.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@ConditionalOnExpression("${sync.init.open:true}")
@Component
public class ScheduledJob {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJob.class);

    @Value("${sync.init.open}")
    private boolean isOpen;

    @Scheduled(cron = "${sync.init.cron}")
    public void scheduleDataSync() {
        LOG.info("----> Scheduled data start ...");
    }
}
