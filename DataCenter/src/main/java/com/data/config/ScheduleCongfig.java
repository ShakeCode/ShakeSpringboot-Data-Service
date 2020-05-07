package com.data.config;


import com.data.job.DataSyncJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//没有配置自己的线程池时，会默认使用SimpleAsyncTaskExecutor。
@Configuration
public class ScheduleCongfig implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleCongfig.class);

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

    @PostConstruct
    private void init(){
       LOG.info("---->init ScheduleCongfig success...");
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(15); //指定线程池大小
    }
}
