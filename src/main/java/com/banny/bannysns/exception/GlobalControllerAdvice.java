package com.banny.bannysns.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseEntity> applicationExceptionHandler(ApplicationException e) {
        log.error("error occurs : {}", e.toString());
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode(), e.getMessage());
    }
}
