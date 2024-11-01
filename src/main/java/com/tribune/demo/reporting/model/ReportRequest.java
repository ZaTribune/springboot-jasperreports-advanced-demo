package com.tribune.demo.reporting.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.util.Map;

@Getter
@Setter
public class ReportRequest {

    @NotNull
    private Long reportId;

    @NotBlank
    private String locale;

    @NotEmpty
    private Map<String,Object> data;
}
