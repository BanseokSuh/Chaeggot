package com.banny.bannysns.controller.response;

import com.banny.bannysns.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<T> {

    private int httpStatus;
    private int code;
    private T result;

    public static Response<Void> error(HttpStatus httpStatus, int code) {
        return new Response<>(httpStatus.value(), code, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>(HttpStatus.OK.value(), ErrorCode.OK.getCode(), result);
    }

    public static Response<Void> success() {
        return new Response<>(HttpStatus.OK.value(), ErrorCode.OK.getCode(), null);
    }
}
