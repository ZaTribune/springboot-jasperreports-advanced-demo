package zatribune.spring.jasperreports.config.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "validation")
public class LocaleProperties {
    private List<String> locales;
}
