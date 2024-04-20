package com.banny.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OK(HttpStatus.OK, 2000, "OK"),
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, 4000, "User ID is duplicated"),
    EMPTY_USER_ID(HttpStatus.BAD_REQUEST, 4001, "User ID is empty"),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, 4002, "User ID is invalid"),
    EMPTY_USER_NAME(HttpStatus.BAD_REQUEST, 4003, "UserName is empty"),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, 4004, "Password is empty"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 4005, "Password is invalid"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 4006, "Password is wrong"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4007, "User not found"),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}

