package com.banny.springboot.controller;

import com.banny.springboot.controller.request.UserJoinRequest;
import com.banny.springboot.controller.request.UserLoginRequest;
import com.banny.springboot.controller.response.Response;
import com.banny.springboot.controller.response.UserJoinResponse;
import com.banny.springboot.controller.response.UserLoginResponse;
import com.banny.springboot.model.User;
import com.banny.springboot.service.UserService;
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
