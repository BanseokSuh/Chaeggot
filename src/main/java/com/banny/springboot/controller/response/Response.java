package com.banny.springboot.controller.response;

import com.banny.springboot.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<T> {

    private int httpStatus;
    private int code;
    private String message;
    private T result;

    public static Response<Void> error(HttpStatus httpStatus, int code, String message) {
        return new Response<>(httpStatus.value(), code, message, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>(HttpStatus.OK.value(), ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(), result);
    }

    public static Response<Void> success() {
        return new Response<>(HttpStatus.OK.value(), ErrorCode.OK.getCode(), ErrorCode.OK.getMessage(),null);
    }

    /**
     * @return JSON string
     */
    public String toStream() {
        if (result == null) {
            return "{" +
                    "\"httpStatus\":" + httpStatus + "," +
                    "\"code\":" + code + "," +
                    "\"message\":" + "\"" + message + "\"," +
                    "\"result\":" + null + "}";
        }

        return "{" +
                "\"httpStatus\":" + httpStatus + "," +
                "\"code\":" + code + "," +
                "\"message\":" + "\"" + message + "\"," +
                "\"result\":" + "\"" + result + "\"" + "}";
    }
}
