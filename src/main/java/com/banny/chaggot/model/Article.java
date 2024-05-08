package com.banny.chaggot.model;

import com.banny.chaggot.model.entity.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long articleIdx;
    private String title;
    private String platform;
    private String url;
    private String memo;
    private User user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Article fromEntity(ArticleEntity entity) {
        Article article = new Article();
        article.setArticleIdx(entity.getArticleIdx());
        article.setTitle(entity.getTitle());
        article.setPlatform(entity.getPlatform());
        article.setUrl(entity.getUrl());
        article.setMemo(entity.getMemo());
        article.setUser(User.fromEntity(entity.getUser()));
        article.setCreatedAt(entity.getCreatedAt());
        article.setUpdatedAt(entity.getUpdatedAt());
        article.setDeletedAt(entity.getDeletedAt());

        return article;
    }
}
