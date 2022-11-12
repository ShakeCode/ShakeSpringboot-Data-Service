package com.data.service.keyword;

import com.data.util.SpringUtils;
import org.springframework.stereotype.Service;

/**
 * The type Key word service.
 */
@Service
public class KeyWordService {

    /**
     * Init data.
     */
    public void initData() {
        BaseKeyWordService baseKeyWordService = (BaseKeyWordService) SpringUtils.getBean(KeyWordServiceEnum.BLACK_PHONE.getServiceName());
        baseKeyWordService.initData();
    }
}
