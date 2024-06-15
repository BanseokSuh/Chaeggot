package com.banny.chaeggot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 20000 : Ok
    OK(HttpStatus.OK, 200000, "Ok"),

    // 40000 ~ 40099 : User
    DUPLICATED_LOGIN_ID(HttpStatus.CONFLICT, 40000, "Login ID is duplicated"),
    EMPTY_LOGIN_ID(HttpStatus.BAD_REQUEST, 40001, "Login ID is empty"),
    INVALID_LOGIN_ID(HttpStatus.BAD_REQUEST, 40002, "Login ID is invalid"),
    EMPTY_USER_NAME(HttpStatus.BAD_REQUEST, 40003, "UserName is empty"),
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, 40004, "Password is empty"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 40005, "Password is invalid"),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, 40006, "Password is wrong"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 40007, "User not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40008, "Token is invalid"),
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, 40009, "Permission is invalid"),

    // 40100 ~ 40199 : Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, 40100, "Post not found"),

    // 40200 ~ 40299 : Article
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, 40200, "Article not found");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}

