package com.data.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Schedule congfig.
 */
//没有配置自己的线程池时，会默认使用SimpleAsyncTaskExecutor。
@Configuration
public class ScheduleCongfig implements SchedulingConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleCongfig.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduledExecutor());
    }

    @PostConstruct
    private void init() {
        LOG.info("---->init ScheduleCongfig success...");
    }

    /**
     * Task executor executor.
     * @return the executor
     */
    @Bean
    public ScheduledExecutorService scheduledExecutor() {
        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(20);
        scheduledExecutor.setMaximumPoolSize(30);
        // 核心线程数（默认线程数）
        scheduledExecutor.setCorePoolSize(20);
        // 线程池对拒绝任务的处理策略
        scheduledExecutor.setRejectedExecutionHandler(new ScheduledThreadPoolExecutor.CallerRunsPolicy());
        scheduledExecutor.setKeepAliveTime(60, TimeUnit.SECONDS);
        return scheduledExecutor;
    }

}
