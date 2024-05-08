package com.banny.chaggot.controller.response;

import com.banny.chaggot.model.User;
import com.banny.chaggot.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    Long userIdx;
    String userId;
    String userName;
    UserRole userRole;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getUserIdx(),
                user.getUserId(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}