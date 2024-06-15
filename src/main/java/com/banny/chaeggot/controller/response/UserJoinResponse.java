package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.entity.User;
import com.banny.chaeggot.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    private Long id;
    private String loginId;
    private String userName;
    private UserRole userRole;

    public static UserJoinResponse from(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getLoginId(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}
