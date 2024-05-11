package com.banny.chaeggot.model;

import com.banny.chaeggot.model.entity.ArticleEntity;
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
    private int viewCount;
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
        article.setViewCount(entity.getViewCount());
        article.setUser(User.fromEntity(entity.getUser()));
        article.setCreatedAt(entity.getCreatedAt());
        article.setUpdatedAt(entity.getUpdatedAt());
        article.setDeletedAt(entity.getDeletedAt());

        return article;
    }
}
