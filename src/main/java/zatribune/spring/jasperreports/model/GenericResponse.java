package zatribune.spring.jasperreports.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object reason;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Object data;
    private int code;
}
