/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.cache;

import com.data.config.CacheManagerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The type Simple cache manager.
 */
public abstract class SimpleCacheManager implements ICacheManager {
    /**
     * The Spring cache manager.
     */
    @Autowired
    protected CacheManagerConfig springCacheManager;

    /**
     * The Spring cache.
     */
    protected Cache springCache;

    /**
     * Gets cache key.
     * @return the cache key
     */
    public abstract String getCacheKey();

    /**
     * Clear.
     */
    @Override
    public void clear() {
        springCache.clear();
    }

    /**
     * Get object.
     * @param key the key
     * @return the object
     */
    @Override
    public Object get(Object key) {
        ValueWrapper valueWrapper = springCache.get(key);
        if (valueWrapper != null) {
            return springCache.get(key).get();
        } else {
            return null;
        }
    }

    /**
     * Init cache data.
     */
    @Override
    public abstract void initCacheData();

    /**
     * Put.
     * @param key   the key
     * @param value the value
     */
    @Override
    public void put(Object key, Object value) {
        springCache.put(key, value);
    }

    /**
     * Refresh.
     */
    @Override
    public void refresh() {
        this.clear();
        this.initCacheData();
    }

    /**
     * Evict.
     * @param key the key
     */
    @Override
    public void evict(Object key) {
        springCache.evict(key);
    }

    /**
     * Put.
     * @param map the map
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void put(Map map) {
        Set<Map.Entry> set = map.entrySet();
        for (Iterator<Map.Entry> it = set.iterator(); it.hasNext(); ) {
            Map.Entry entry = it.next();
            this.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 执行先后顺序: Constructor > @PostConstruct > InitializingBean > init-method
     */
    @Override
    @PostConstruct
    public void initCache() {
        springCache = this.springCacheManager.ehCacheCacheManager().getCache(getCacheKey());
        if (this.springCache == null) {
            throw new RuntimeException("请检查ehcache.xml 中是否配置 name=" + getCacheKey());
        }
        initCacheData();
    }
}
