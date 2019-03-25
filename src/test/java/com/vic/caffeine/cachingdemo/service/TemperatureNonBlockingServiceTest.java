package com.vic.caffeine.cachingdemo.service;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.verification.VerificationMode;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TemperatureNonBlockingServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    WeatherStation weatherStation;

    @InjectMocks
    TemperatureNonBlockingService service;

    @Test
    public void shuouldCallWeatherStation() {
        service.getTemperatureForCity("Tel-Aviv");
        verify(weatherStation).getTemperature("Tel-Aviv");
    }


    /**
     * The cache works since the cache is manually initialized and used in TemperatureNonBlockingService
     */
    @Test
    public void shuouldGetCachedTemperature() {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 35", 35, taTemperature);

        when(weatherStation.getTemperature(any())).thenReturn(40);
        taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 35 (cached!!!)", 35, taTemperature);
    }


    /**
     * The cache works since the cache is manually initialized and used in TemperatureNonBlockingService
     */
    @Test
    public void shuouldRereshCacheAsynchroniusly() throws InterruptedException {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 35", 35, taTemperature);

        Thread.sleep(2000);

        when(weatherStation.getTemperature(any())).then((value)-> {
            sleep(2000);
            return 55;
        });


        taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 35 (old)", 35, taTemperature);

        sleep(3000);
        taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 55 (fresh)", 55, taTemperature);

    }

    private static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}