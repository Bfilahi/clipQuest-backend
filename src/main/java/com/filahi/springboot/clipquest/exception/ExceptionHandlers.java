package com.filahi.springboot.clipquest.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleException(ResponseStatusException ex) {
        return buildResponseEntity(ex, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception ex, HttpStatus status) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                status.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(exceptionResponse, status);
    }
}
