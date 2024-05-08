package com.banny.chaggot.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    String token;

    public static UserLoginResponse fromToken(String token) {
        return new UserLoginResponse(token);
    }
}
