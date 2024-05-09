package com.banny.chaeggot.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Builder
@Data
public class ErrorResponseEntity {
    private int httpStatus;
    private int code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .httpStatus(errorCode.getHttpStatus().value())
                        .code(errorCode.getCode())
                        .message(message)
                        .build());
    }
}