package com.data.service.keyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("sensitiveService")
public class SensitiveService implements BaseKeyWordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveService.class);

    /**
     * Init data.
     */
    @Override
    public void initData() {
        LOGGER.info("Init Sensitive Word start...");
    }
}
