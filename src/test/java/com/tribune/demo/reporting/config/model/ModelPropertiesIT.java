package com.tribune.demo.reporting.config.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest(classes = LocaleProperties.class)
@EnableConfigurationProperties(LocaleProperties.class)
class ModelPropertiesIT {

    @Autowired
    LocaleProperties localeProperties;



    @Test
    void testLocales(){

        assertNotNull(localeProperties);
        assertNotEquals(0,localeProperties.getLocales().size());
        log.info("locales: {}",localeProperties.getLocales());
    }

}