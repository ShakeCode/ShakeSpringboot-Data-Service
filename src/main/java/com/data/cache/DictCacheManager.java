/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.cache;


import com.data.model.entity.disctionary.DictDO;
import com.data.service.master.DictionaryService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * The type Dict cache manager.
 */
@Component
public class DictCacheManager extends SimpleCacheManager {
    private static final Logger logger = LoggerFactory.getLogger(DictCacheManager.class);

    @Autowired
    private DictionaryService dictService;

    /**
     * Gets cache key.
     * @return the cache key
     */
    @Override
    public String getCacheKey() {
        return "dict_cache";
    }

    /**
     * Init cache data.
     */
    @Override
    public void initCacheData() {
        logger.debug("###### 初始化 字典 数据开始 ######");
        List<DictDO> dictList = Lists.newArrayList(new DictDO("showName", "true", new Date()), new DictDO("syncEnable", "true", new Date()));
        for (DictDO dict : dictList) {
            if (dict != null) {
                this.springCache.put(dict.getCode(), dict.getValue());
            }
        }
        logger.debug("###### 初始化 字典 数据结束 ######");
    }

    /**
     * 刷新单条记录
     * @param key the key
     */
    public void refresh(String key) {
        if (key == null) {
            return;
        }
        String dictCode = key.trim();
        if ("".equals(dictCode)) {
            return;
        }
        DictDO dict = new DictDO(key, "false", new Date());
        this.evict(dict.getCode());
        this.put(dict.getCode(), dict.getValue());
    }

}
