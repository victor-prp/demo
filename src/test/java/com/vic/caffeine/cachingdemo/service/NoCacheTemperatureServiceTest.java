package com.vic.caffeine.cachingdemo.service;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.verify;


public class NoCacheTemperatureServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    WeatherStation weatherStation;

    @InjectMocks
    TemperatureService service;

    @Test
    public void shuouldCallWeatherStation() {
        service.getTemperatureForCity("Tel-Aviv");
        verify(weatherStation).getTemperature("Tel-Aviv");
    }


    /**
     * The cache is not working since it is not running by Spring boot
     */
    @Test
    public void shuouldGetFreshTemperature() {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35", 35, taTemperature);

        when(weatherStation.getTemperature(any())).thenReturn(40);
        taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 40 (not cached)", 40, taTemperature);
    }

}