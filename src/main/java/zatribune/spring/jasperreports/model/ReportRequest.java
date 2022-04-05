package zatribune.spring.jasperreports.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@Setter
public class ReportRequest {

    @NotBlank(message = "{NotBlank}")
    private String reportName;

    @NotBlank(message = "{NotBlank}")
    private String locale;

    @NotEmpty(message = "{NotEmpty}")
    private Map<String,Object> data;

    private ReportExportType exportType;
}
