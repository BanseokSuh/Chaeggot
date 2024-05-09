package com.banny.chaeggot.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.sql.Timestamp;

@Setter
@Getter
@SQLDelete(sql = "UPDATE \"article\" SET deleted_at = NOW() WHERE article_idx = ?")
@SQLRestriction("deleted_at IS NULL")
@Table(name = "\"article\"")
@Entity
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_idx", nullable = false)
    private Long articleIdx;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "platform", nullable = false)
    private String platform;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

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

}
