package com.vic.caffeine.cachingdemo.it;

import com.vic.caffeine.cachingdemo.service.TemperatureService;
import com.vic.caffeine.cachingdemo.service.WeatherStation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemperatureServiceIntegrationTest {
    @MockBean
    WeatherStation weatherStation;

    @Autowired
    TemperatureService service;

    /**
     * The cache is working since it is running by Spring boot
     */
    @Test
    public void shuouldGetCachedTemperature() {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35 (initial)", 35, taTemperature);

        when(weatherStation.getTemperature(any())).thenReturn(40);
        taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 35 (cached!!!!)", 35, taTemperature);
    }


    /**
     * The cache is working since it is running by Spring boot
     */
    @Test
    public void shuouldGetFreshTemperatureAfterExpiration() throws InterruptedException {
        when(weatherStation.getTemperature(any())).thenReturn(35);
        int taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temparature should be 35 (initial)", 35, taTemperature);

       Thread.sleep(8000); //The expiration is 5 seconds, so 8 seconds must trigger cache renewal
        when(weatherStation.getTemperature(any())).thenReturn(40);
        taTemperature = service.getTemperatureForCity("Tel-Aviv");
        Assert.assertEquals("TelAviv temperature should be 40 (fresh value after expiration)", 40, taTemperature);
    }


}
