package com.banny.bannysns.controller;

import com.banny.bannysns.controller.request.UserJoinRequest;
import com.banny.bannysns.controller.request.UserLoginRequest;
import com.banny.bannysns.controller.response.Response;
import com.banny.bannysns.controller.response.UserJoinResponse;
import com.banny.bannysns.model.User;
import com.banny.bannysns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserId(), request.getUserName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("login")
    public Response<String> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserId(), request.getPassword());
        return Response.success(token);
    }
}
