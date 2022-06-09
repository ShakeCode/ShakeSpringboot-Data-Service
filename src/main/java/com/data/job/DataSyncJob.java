package com.data.job;

import com.data.service.custer.UserService;
import com.data.util.SpringUtils;
import com.data.util.ThreadLocalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSyncJob implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataSyncJob.class);

    @Override
    public void run(String... strings) throws Exception {
        LOG.info("----->DataSyncJob start... ");
        ThreadLocalUtils.setValue("this is local thread");
        LOG.error(" user-service get by springutils:{}", SpringUtils.getBean(UserService.class));
        LOG.info("thread-local value:{}",ThreadLocalUtils.getValue());
    }
}
