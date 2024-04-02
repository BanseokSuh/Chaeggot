package com.banny.bannysns.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private int httpStatus;
    private int code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode, String message){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .httpStatus(errorCode.getHttpStatus().value())
                        .code(errorCode.getCode())
                        .message(message)
                        .build());
    }
}