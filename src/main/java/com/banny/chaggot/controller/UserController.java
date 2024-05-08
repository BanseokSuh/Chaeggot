package com.banny.chaggot.controller;

import com.banny.chaggot.controller.request.UserJoinRequest;
import com.banny.chaggot.controller.request.UserLoginRequest;
import com.banny.chaggot.controller.response.Response;
import com.banny.chaggot.controller.response.UserJoinResponse;
import com.banny.chaggot.controller.response.UserLoginResponse;
import com.banny.chaggot.model.User;
import com.banny.chaggot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * Join
     *
     * @param request
     * @return
     */
    @PostMapping("join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getUserId(), request.getUserName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    /**
     * Login
     *
     * @param request
     * @return
     */
    @PostMapping("login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserId(), request.getPassword());
        return Response.success(UserLoginResponse.fromToken(token));
    }
}
