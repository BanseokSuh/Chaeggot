package com.banny.chaeggot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }
}
