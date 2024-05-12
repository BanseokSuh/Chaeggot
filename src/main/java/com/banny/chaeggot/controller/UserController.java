package com.banny.chaeggot.controller;

import com.banny.chaeggot.controller.request.UserJoinRequest;
import com.banny.chaeggot.controller.request.UserLoginRequest;
import com.banny.chaeggot.controller.response.Response;
import com.banny.chaeggot.controller.response.UserJoinResponse;
import com.banny.chaeggot.controller.response.UserLoginResponse;
import com.banny.chaeggot.model.User;
import com.banny.chaeggot.service.UserService;
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
        return Response.success(UserJoinResponse.from(user));
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
