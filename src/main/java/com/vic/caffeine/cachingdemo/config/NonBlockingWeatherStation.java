package com.vic.caffeine.cachingdemo.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.vic.caffeine.cachingdemo.service.WeatherStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
public class NonBlockingWeatherStation {
    public static final String TEMPERATURE_NON_BLOCKING_CACHE = "temperature_non_blocking";

    private final WeatherStation weatherStation;

    private final Cache<String, Integer> cache;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Value("${cache.expire.temperature.seconds:5}")
    private int expiryTemperatureSeconds = 5;

    public NonBlockingWeatherStation(WeatherStation weatherStation) {
        this.weatherStation = weatherStation;
        cache = buildTemperatureCache();
    }


    public int getTemperature(String city){
        Integer current = cache.getIfPresent(city);
        if(current == null){
            return cache.get(city, weatherStation::getTemperature);
        }
        return current;
    }

    private Cache buildTemperatureCache() {
        // no checked exception
        return Caffeine.newBuilder()
                .expireAfterAccess(expiryTemperatureSeconds, TimeUnit.SECONDS)
                .build();


    }

}