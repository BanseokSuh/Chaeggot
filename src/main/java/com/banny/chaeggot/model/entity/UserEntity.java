package com.banny.chaeggot.model.entity;

import com.banny.chaeggot.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = NOW() WHERE user_idx = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "\"user\"", uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_USER_ID", columnNames = "user_id"),
        @UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = "email")
})
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx", nullable = false)
    private Long userIdx;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(String userId, String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }
}
