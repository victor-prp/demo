package com.vic.caffeine.cachingdemo.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    public static final String TEMPERATURE_CACHE = "temperature";

    @Value("${cache.expire.temperature.seconds:5}")
    private int expiryTemperatureSeconds;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Collections.singletonList(
                buildTemperatureCache()
        ));
        return simpleCacheManager;
    }

    private CaffeineCache buildTemperatureCache() {
        Cache cache = Caffeine.newBuilder()
                .expireAfterAccess(expiryTemperatureSeconds, TimeUnit.SECONDS)
                .build();

        return new CaffeineCache(TEMPERATURE_CACHE, cache, false);
    }



}
