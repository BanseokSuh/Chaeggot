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
@SQLDelete(sql = "UPDATE \"user\" SET deleted_at = NOW() WHERE user_id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "\"user\"", uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_USER_ID", columnNames = "login_id"),
        @UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = "email")
})
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false)
    private String loginId;

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

    public static UserEntity of(String loginId, String userName, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId(loginId);
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
