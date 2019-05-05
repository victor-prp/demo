package com.vic.profiles;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("int")
public class IntProfileIntegrationTest {
    @Autowired
    ProductionService productionService;

    @Test
    public void shouldAssignDbDomainForDev() {
        Assert.assertEquals("int.poalim.com",productionService.getDbDomain());
    }
}