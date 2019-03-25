package com.vic.caffeine.cachingdemo.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.vic.caffeine.cachingdemo.service.WeatherStation;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    private static Logger log = LoggerFactory.getLogger(CacheConfig.class);

    public static final String TEMPERATURE_CACHE = "temperature";

    @Autowired
    private WeatherStation weatherStation;

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
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(loader());

        return new CaffeineCache(TEMPERATURE_CACHE, cache, false);
    }

    CacheLoader<String, Integer> loader(){
        return new CacheLoader<String, Integer>() {
            @Override
            public Integer load(@NonNull String key) throws Exception {
                log.info("load for: {}", key);
                return weatherStation.getTemperature(key);
            }

            @Override
            public @NonNull CompletableFuture<Integer> asyncReload(@NonNull String key, @NonNull Integer oldValue, @NonNull Executor executor) {
                log.info("reload for: {}", key);
                return CompletableFuture.supplyAsync(() -> weatherStation.getTemperature(key), executor);
            }

        };
    }

}
