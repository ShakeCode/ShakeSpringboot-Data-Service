package com.data.service.keyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The type Black phone serivce.
 */
@Service("blackPhoneSerivce")
public class BlackPhoneSerivce implements BaseKeyWordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackPhoneSerivce.class);

    /**
     * Init data.
     */
    @Override
    public void initData() {
        LOGGER.info("Init Black Phone Start ...");
    }
}
