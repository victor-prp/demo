package com.vic.caffeine.cachingdemo.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TemperatureNonBlockingCaffeinService {
    private static Logger log = LoggerFactory.getLogger(TemperatureNonBlockingCaffeinService.class);

    private final WeatherStation weatherStation;
    private final LoadingCache<String, Integer> cache;

    public TemperatureNonBlockingCaffeinService(WeatherStation weatherStation) {
        this.weatherStation = weatherStation;

        cache = Caffeine.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(loader());
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

    public int getTemperatureForCity(String city) {
        log.info("NonBlocking: Getting temperature from weather station for: {} ", city);
        return getFromCache(city);
    }


    private Integer getFromCache(String city) {
        return cache.get(city);
    }
}
