package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.model.User;
import com.banny.chaeggot.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userIdx;
    private String userId;
    private UserRole userRole;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getUserIdx(),
                user.getUserId(),
                user.getUserRole()
        );
    }
}
