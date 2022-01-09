package zatribune.spring.jasperreports.config.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class ModelPropertiesTest {

    @Autowired
    private LocaleProperties localeProperties;



    @Test
    void testLocales(){

        assertNotNull(localeProperties);
        assertNotEquals(0,localeProperties.getLocales().size());
        log.info("locales: {}",localeProperties.getLocales());


    }

}