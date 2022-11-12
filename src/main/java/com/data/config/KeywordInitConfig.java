package com.data.config;

import com.data.service.keyword.KeyWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Configuration;

/**
 * The type Keyword init config. 初始化Bean容器的后置处理,此时端口还没打开,做完后置处理后端口继续打开
 */
@Configuration
public class KeywordInitConfig implements SmartInitializingSingleton {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordInitConfig.class);

    private final KeyWordService keyWordService;

    /**
     * Instantiates a new Keyword init config.
     * @param keyWordService the key word service
     */
    public KeywordInitConfig(KeyWordService keyWordService) {
        this.keyWordService = keyWordService;
    }

    /**
     * Invoked right at the end of the singleton pre-instantiation phase,
     * with a guarantee that all regular singleton beans have been created
     * already. {@link ListableBeanFactory#getBeansOfType} calls within
     * this method won't trigger accidental side effects during bootstrap.
     * <p><b>NOTE:</b> This callback won't be triggered for singleton beans
     * lazily initialized on demand after {@link BeanFactory} bootstrap,
     * and not for any other bean scope either. Carefully use it for beans
     * with the intended bootstrap semantics only.
     */
    @Override
    public void afterSingletonsInstantiated() {
        LOGGER.info("Init Bean Success, Begin Sync Keyword...");
        keyWordService.initData();
    }
}
