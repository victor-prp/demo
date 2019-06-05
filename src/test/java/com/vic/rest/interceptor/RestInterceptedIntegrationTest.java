package com.vic.rest.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RestInterceptedIntegrationTest {
    RestTemplate restTemplate;

    @Before
    public void init(){
        restTemplate = new RestTemplate();
    }

    @Test
    public void shouldIncCounterAfterReqCompletion() throws InterruptedException {
        InterceptedRes resOK0 = restTemplate.postForObject("http://localhost:8080/rest/api/intercepted",new InterceptedReq("OK"), InterceptedRes.class);
        Assert.assertEquals(0, (int)resOK0.getCount());
        Thread.sleep(200);

        InterceptedRes resOK1 = restTemplate.postForObject("http://localhost:8080/rest/api/intercepted",new InterceptedReq("OK"), InterceptedRes.class);
        Assert.assertEquals(1, (int)resOK1.getCount());
        Thread.sleep(100);

        InterceptedRes resOK2 = restTemplate.postForObject("http://localhost:8080/rest/api/intercepted",new InterceptedReq("OK"), InterceptedRes.class);
        Assert.assertEquals(2, (int)resOK2.getCount());
        Thread.sleep(100);
    }

    /**
     * This test fails since server cannot be aware of the client side timeout see:
     * https://stackoverflow.com/questions/35309513/what-happens-at-the-server-when-client-closes-the-connection-by-using-readtimeou
     */
//    @Test
//    public void shouldNOTIncCounterIfResponseWasNotProperlyRead() throws InterruptedException, IOException {
//        try {
//            val restTemplateWith0Timeout = new RestTemplateBuilder()
//                    .setReadTimeout(Duration.ofMillis(50))
//                    .setConnectTimeout(Duration.ofMillis(50))
//                    .build();
//            val resOK0 = restTemplateWith0Timeout.postForObject("http://localhost:8080/rest/api/intercepted", new InterceptedReq("OK"), InterceptedRes.class);
//            log.info("resOK0: {}",resOK0);
//        } catch (ResourceAccessException timeout) {
//            log.info("Timed out", timeout);
//            //Expected
//        }
//
//        Thread.sleep(1000);
//        log.info("Going to call resOK1");
//
//
//        InterceptedRes resOK1 = restTemplate.postForObject("http://localhost:8080/rest/api/intercepted",new InterceptedReq("OK"), InterceptedRes.class);
//        log.info("resOK1: {}",resOK1);
//
//        Assert.assertEquals("should remain 0 since the first req was timed out", 0, (int)resOK1.getCount());
//        Thread.sleep(100);
//
//    }

}