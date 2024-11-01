package com.tribune.demo.reporting.config.jasperreports;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jasperreports.pdf.report")
public class PdfReportConfig {

    private Boolean sizePageToContent;
    private Boolean forceLineBreakPolicy;
}
