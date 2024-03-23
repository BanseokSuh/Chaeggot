package com.banny.springboota.controller.response;

import com.banny.springboota.model.User;
import com.banny.springboota.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {
    Long id;
    String userId;
    String userName;
    UserRole userRole;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUserId(),
                user.getUserName(),
                user.getUserRole()
        );
    }
}
