package zatribune.spring.jasperreports.config.jasperreports;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jasperreports.pdf.export")
public class PdfExportConfig {

    private String metadataAuthor;
    private Boolean reportEncrypted;
    private String allowedPermissionsHint;
    private Boolean reportCompressed;
}
