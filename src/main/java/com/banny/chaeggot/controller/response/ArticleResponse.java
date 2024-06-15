package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    private Long id;
    private String title;
    private String url;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getUrl(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
