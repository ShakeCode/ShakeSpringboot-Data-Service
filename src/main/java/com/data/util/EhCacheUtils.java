/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.util;


import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


/**
 * The type Eh cache utils.
 */
@Component
public class EhCacheUtils {
    private static EhCacheCacheManager ehCacheCacheManager;

    /**
     * Sets manager.
     * @param manager the manager
     */
    @Autowired
    public void setManager(EhCacheCacheManager manager) {
        EhCacheUtils.ehCacheCacheManager = manager;
    }

    /**
     * Put.
     * @param cacheName the cache name 添加本地缓存 (相同的key 会直接覆盖)
     * @param key       the key
     * @param value     the value
     */
    public void put(String cacheName, String key, Object value) {
        Cache cache = getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * Put.
     * @param cacheName the cache name
     * @param key       the key
     * @param value     the value
     * @param ttl       the ttl
     */
    public static void put(String cacheName, String key, Object value, Integer ttl) {
        Cache cache = getCache(cacheName);
        Element element = new Element(key, value);
        //不设置则使用xml配置
        if (ttl != null) {
            element.setTimeToLive(ttl);
        }
        cache.put(element);
    }

    /**
     * Get object.   获取本地缓存
     * @param cacheName the cache name
     * @param key       the key
     * @return the object
     */
    public Object get(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * Remove.
     * @param cacheName the cache name
     * @param key       the key
     */
    public void remove(String cacheName, String key) {
        Cache cache = getCache(cacheName);
        cache.remove(key);
    }

    /**
     * Remove all.
     * @param cacheName the cache name
     */
    public void removeAll(String cacheName) {
        Cache cache = getCache(cacheName);
        cache.removeAll();
    }

    private static Cache getCache(String cacheName) {
        CacheManager cacheManager = ehCacheCacheManager.getCacheManager();
        if (!cacheManager.cacheExists(cacheName)) {
            cacheManager.addCache(cacheName);
        }
        return cacheManager.getCache(cacheName);
    }

}