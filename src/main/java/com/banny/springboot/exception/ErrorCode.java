package com.banny.springboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    OK(HttpStatus.OK, 200000, "Ok"),
    // 4000 ~ 4099 : User
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, 400000, "User ID is duplicated"),
    EMPTY_USER_ID(HttpStatus.BAD_REQUEST, 400001, "User ID is empty"),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, 400002, "User ID is invalid"),
    EMPTY_USER_NAME(HttpStatus.BAD_REQUEST, 400003, "UserName is empty"),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, 400004, "Password is empty"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 400005, "Password is invalid"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 400006, "Password is wrong"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 400007, "User not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 400008, "Token is invalid"),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, 400009, "Permission is invalid"),

    // 4100 ~ 4199 : Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, 400100, "Post not found"),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}

