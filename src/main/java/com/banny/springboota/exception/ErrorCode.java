package com.banny.springboota.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "001", "User ID is duplicated"),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "002", "User ID is invalid"),
    INVALID_USER_NAME(HttpStatus.BAD_REQUEST, "003", "User name is invalid"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "004", "Password is invalid")
    ;

    private HttpStatus httpStatus;
    private String code;
    private String message;
}
