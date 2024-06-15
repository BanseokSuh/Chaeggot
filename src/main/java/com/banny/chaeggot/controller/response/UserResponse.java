package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.entity.User;
import com.banny.chaeggot.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String loginId;
    private UserRole userRole;

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLoginId(),
                user.getUserRole()
        );
    }
}
