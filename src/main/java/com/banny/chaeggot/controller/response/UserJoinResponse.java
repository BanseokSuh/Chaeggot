package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.model.User;
import com.banny.chaeggot.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    Long id;
    String loginId;
    String userName;
    UserRole userRole;

    public static UserJoinResponse from(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getLoginId(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}
