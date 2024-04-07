package com.banny.bannysns.model;

import com.banny.bannysns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private Long id;
    private String userId;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setUserId(entity.getUserId());
        user.setUserName(entity.getUserName());
        user.setPassword(entity.getPassword());
        user.setUserRole(entity.getUserRole());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setDeletedAt(entity.getDeletedAt());

        return user;
    }
}
