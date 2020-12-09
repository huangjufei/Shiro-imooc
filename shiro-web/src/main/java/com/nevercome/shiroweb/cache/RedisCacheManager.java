package com.nevercome.shiroweb.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 在配置文件直接引入它
 */
public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisCache redisCache;

    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return redisCache;
    }
}
