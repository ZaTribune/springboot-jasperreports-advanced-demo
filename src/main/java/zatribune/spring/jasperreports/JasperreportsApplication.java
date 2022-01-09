package zatribune.spring.jasperreports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("zatribune.spring.jasperreports.config")
@SpringBootApplication
public class JasperreportsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JasperreportsApplication.class, args);
    }

}
