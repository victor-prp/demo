package com.vic.caffeine.cachingdemo.service;

import com.vic.caffeine.cachingdemo.config.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {
    private static Logger log = LoggerFactory.getLogger(TemperatureService.class);

    private final WeatherStation weatherStation;

    public TemperatureService(WeatherStation weatherStation) {
        this.weatherStation = weatherStation;
    }

    @Cacheable(CacheConfig.TEMPERATURE_CACHE)
    public int getTemperatureForCity(String city) {
        log.info("Getting temperature from weather station for: {} ", city);
        return weatherStation.getTemperature(city);
    }
}
