package com.vic.caffeine.cachingdemo.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class TemperatureNonBlockingService {
    private static Logger log = LoggerFactory.getLogger(TemperatureNonBlockingService.class);

    private final WeatherStation weatherStation;
    private final LoadingCache<String, Integer> cache;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public TemperatureNonBlockingService(WeatherStation weatherStation) {
        this.weatherStation = weatherStation;

        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(loader());
    }


    public void update(String city, Integer temperature){
        cache.put(city, temperature);
    }

    CacheLoader<String, Integer> loader(){
        return new CacheLoader<String, Integer>() {
            @Override
            public Integer load(@NonNull String key) throws Exception {
                log.info("load for: {}", key);
                return weatherStation.getTemperature(key);
            }

            @Override
            public ListenableFuture<Integer> reload(@NonNull String key, @NonNull Integer oldValue) throws Exception {
                log.info("reload for: {}", key);
                ListenableFutureTask<Integer> task = ListenableFutureTask.create(() -> weatherStation.getTemperature(key));
                executor.execute(task);
                return task;
            }
        };
    }

    public int getTemperatureForCity(String city) {
        log.info("NonBlocking: Getting temperature from weather station for: {} ", city);
        return getFromCache(city);
    }


    private Integer getFromCache(String city) {
        try {
            return cache.get(city);
        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to load the value for: "+city,e);
        }
    }
}
