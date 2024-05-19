package com.banny.chaeggot.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Setter
@Getter
@SQLDelete(sql = "UPDATE \"post\" SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "\"post\"")
@Entity
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "TEXT")
    private String title;

    @Column(name = "content")
    private String content;

    // @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    public static PostEntity of(String title, String content, UserEntity user) {
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setUser(user);

        return postEntity;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.deletedAt = new Timestamp(System.currentTimeMillis());
    }
}
