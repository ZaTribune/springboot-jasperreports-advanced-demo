package zatribune.spring.jasperreports.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class ReportRequest {

    @NotNull(message = "{NotNull}")
    private Long reportId;

    @NotBlank(message = "{NotBlank}")
    private String locale;

    @NotEmpty(message = "{NotEmpty}")
    private Map<String,Object> data;
}
