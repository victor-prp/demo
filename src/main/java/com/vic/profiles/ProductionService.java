package com.vic.profiles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductionService {

    private final String dbDomain;

    public ProductionService(@Value("${demo.dbDomain}") String dbDomain,
                             Environment env) {
        this.dbDomain = dbDomain;
        log.info("active profiles: {}", env.getActiveProfiles());
        log.info("dbDomain: {}", dbDomain);
    }

    public String getDbDomain() {
        return dbDomain;
    }
}
