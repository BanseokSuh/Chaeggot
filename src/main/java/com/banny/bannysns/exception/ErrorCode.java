package com.banny.bannysns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_ID(HttpStatus.CONFLICT, 4000), // "User ID is duplicated"
    EMPTY_USER_ID(HttpStatus.BAD_REQUEST, 4001), // "User ID is empty"
    INVALID_USER_ID_LENGTH(HttpStatus.BAD_REQUEST, 4002), // "User ID is too short"
    EMPTY_USER_NAME(HttpStatus.BAD_REQUEST, 4003), // "Username is empty"
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, 4004), // "Password is blank"
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 4005), // "Password is invalid"
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 4006), // "User not found"
    ;

    private HttpStatus httpStatus;
    private Integer code;
}
