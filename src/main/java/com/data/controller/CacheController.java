/*
 * Copyright © 2022 AliMa, Inc. Built with Dream
 */

package com.data.controller;

import com.data.cache.DictCacheManager;
import com.data.config.CacheManagerConfig;
import com.data.model.entity.disctionary.DictDO;
import com.data.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * The type Cache controller.
 */
@RequestMapping("v1/cache")
@RestController
public class CacheController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);

    private final DictCacheManager dictCacheManager;

    private final RedisUtil redisUtil;

    /**
     * Instantiates a new Cache controller.
     * @param dictCacheManager the dict cache manager
     * @param redisUtil        the redis util
     */
    public CacheController(DictCacheManager dictCacheManager, RedisUtil redisUtil) {
        this.dictCacheManager = dictCacheManager;
        this.redisUtil = redisUtil;
    }

    /**
     * Gets ehcache.       cacheManager：缓存管理器, 支持ehcache和redis，若不添加该属性默认缓存到redis
     * Cacheable属性解释
     * cacheNames：缓存名称   和 ehcache.xml的声明一致
     * key：缓存键
     * cacheManager：缓存管理器，支持ehcache和redis，若不添加该属性默认缓存到redis
     * 可选参数：   (未指定时, springboot根据缓存类型 进行多级缓存)
     * 1.CacheManagerConfig.CacheManagerNames.EHCACHE_CACHE_MANAGER、
     * 2.CacheManagerConfig.CacheManagerNames.REDIS_CACHE_MANAGER
     * unless：条件判断，代码给出的条件是结果不为null才加入到缓存
     * @param code the code
     * @return the ehcache
     */
    @Cacheable(cacheNames = "user_ehcache", key = "#code", cacheManager = CacheManagerConfig.CacheManagerNames.EHCACHE_CACHE_MAANGER, unless = "#result==null")
    @RequestMapping("/ehcache/get")
    @ResponseBody
    public String getEhcache(String code) {
        String result = "cache:" + code;
        // 未缓存打印以下日志
        LOGGER.info("ehcache data:{}", result);
        return result;
    }

    /**
     * Gets redis.
     * @param code the code
     * @return the redis
     */
    @Cacheable(key = "#code", value = "user_redis", cacheManager = CacheManagerConfig.CacheManagerNames.REDIS_CACHE_MANAGER)
    @RequestMapping("/redis/get")
    @ResponseBody
    public String getRedisCache(String code) {
        String result = "cache:" + code;
        LOGGER.info("redis Cache data:{}", result);
        return result;
    }

    /**
     * Gets second cache.
     * @param code the code
     * @return the second cache
     */
    @Cacheable(key = "#code", value = "mutil_cache_user")
    @RequestMapping("/mutil/cache/get")
    @ResponseBody
    public DictDO getSecondCache(String code) {
        LOGGER.error("ehcache dir:{}", System.getProperty("java.io.tmpdir/ehcache-rmi-4000"));
        DictDO tbCache = new DictDO(code, "new redis data", new Date());
        LOGGER.info("mutil Cache data:{}", tbCache);
        return tbCache;
    }

    /**
     * Gets e.
     * @param code the code
     * @return the e
     */
    @ApiOperation(value = "二级缓存获取字典数据", tags = "缓存穿透击穿")
    @GetMapping("/dict/ehcache")
    @ResponseBody
    public Object getCacheData(String code) {
        if (dictCacheManager.get(code) != null) {
            LOGGER.info("————    查询Ehcache缓存   ————");
            return dictCacheManager.get(code);
        } else {
            LOGGER.info("————    查询DB数据库   ————");
            // 查询数据库
            DictDO tbCache = new DictDO(code, "new ehcache data", new Date());
            dictCacheManager.put(code, tbCache);
            return tbCache;
        }
    }

    /**
     * Get object.
     * @param code the code
     * @return the object
     */
    @ApiOperation(value = "三级缓存获取字典数据", tags = "防止缓存穿透击穿")
    @GetMapping("/dict/redis")
    @ResponseBody
    public Object get(String code) {
        if (dictCacheManager.get(code) != null) {
            LOGGER.info("————    查询Ehcache缓存  不为空 ————");
            return dictCacheManager.get(code);
        }
        if (redisUtil.hasKey(code)) {
            LOGGER.info("————    查询Redis数据库  不为空 ————");
            LOGGER.info("redis key: {}, 过期时间:{}", code, redisUtil.getExpire(code));
            return redisUtil.get(code);
        }
        LOGGER.info("————    查询DB数据库   ————");
        DictDO tbCache = new DictDO(code, "new redis data", new Date());
        redisUtil.set(code, tbCache);
        dictCacheManager.put(code, tbCache);
        return tbCache;
    }

}
