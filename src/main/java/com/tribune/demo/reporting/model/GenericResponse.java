package com.tribune.demo.reporting.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import lombok.*;

@Builder
public record GenericResponse<T>(@Email
                                 String message,
                                 @JsonInclude(JsonInclude.Include.NON_EMPTY)
                                 Object reason,
                                 @JsonInclude(JsonInclude.Include.NON_EMPTY)
                                 T data,
                                 int code) {


}
