package com.vic.caffeine.cachingdemo.service;

import com.vic.caffeine.cachingdemo.config.NonBlockingWeatherStation;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NonBlockingWeatherStationTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    WeatherStation weatherStation;

    @InjectMocks
    NonBlockingWeatherStation service;

    @Test
    public void shuouldCallWeatherStation() {
        service.getTemperature("Tel-Aviv");
        verify(weatherStation).getTemperature("Tel-Aviv");
    }


    /**
     * The cache works since it is used manually inside NonBlockingWeatherStation
     */
    @Test
    public void shuouldGetCachedTemperature() {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperature("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35", 35, taTemperature);

        when(weatherStation.getTemperature(any())).thenReturn(40);
        taTemperature = service.getTemperature("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35 (cached!!!)", 35, taTemperature);
    }

    /**
     * The cache works since it is used manually inside NonBlockingWeatherStation
     */
    @Test
    public void shouldReloadToCacheAsynchronously() throws InterruptedException {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperature("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35", 35, taTemperature);

        Thread.sleep(8000);

        when(weatherStation.getTemperature(any())).thenReturn(40);
        taTemperature = service.getTemperature("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35 (cached!!!)", 35, taTemperature);
    }
}
