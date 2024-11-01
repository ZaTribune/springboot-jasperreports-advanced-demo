package com.tribune.demo.reporting.error;


import net.sf.jasperreports.engine.JRException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.tribune.demo.reporting.model.GenericResponse;

import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnsupportedOperationException.class)
    ResponseEntity<String> handleException(UnsupportedOperationException ex) {
        //using [ResponseEntity<> class] = using [@ResponseStatus+@ResponseBody]
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_IMPLEMENTED);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JRException.class)
    ResponseEntity<GenericResponse> handleException(JRException ex) {

        logger.error(ex.getMessageKey());
        GenericResponse response = GenericResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getCause() == null ? new String[]{ex.getLocalizedMessage()} : new String[]{ex.getCause().getMessage()})
                .code(6001)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedItemException.class)
    ResponseEntity<GenericResponse> handleException(UnsupportedItemException ex) {

        logger.error(ex.getMessage());
        GenericResponse response = GenericResponse.builder()
                .message(ex.getMessage())
                .reason(ex.getCause() == null ? new String[]{ex.getLocalizedMessage()} : new String[]{ex.getCause().getMessage()})
                .code(6002)
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        logger.error("error");
        List<Object> list = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s : %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        logger.error(list);

        GenericResponse response = GenericResponse.builder()
                .message("Validation Error")
                .reason(list.toArray())
                .code(6002)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

}
