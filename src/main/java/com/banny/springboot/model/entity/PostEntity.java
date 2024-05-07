package com.banny.springboot.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;

@Setter
@Getter
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() WHERE post_idx = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "\"post\"")
@Entity
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_idx", nullable = false)
    private Long postIdx;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private UserEntity user;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void createdAt() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public static PostEntity of(String title, String content, UserEntity user) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setUser(user);

        return postEntity;
    }
}
