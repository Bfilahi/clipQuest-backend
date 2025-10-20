package com.filahi.springboot.clipquest.exception;


public record ExceptionResponse(
        int status,
        String message,
        long timestamp
) {
}
