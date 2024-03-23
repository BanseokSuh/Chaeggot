package com.banny.bannysns.controller.request;

import lombok.Getter;

@Getter
public class UserJoinRequest {
    private String userId;
    private String userName;
    private String password;
}
