package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.model.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleResponse {

    private Long articleIdx;
    private String title;
    private String url;

    public static ArticleResponse from(Article article) {
        return new ArticleResponse(
                article.getArticleIdx(),
                article.getTitle(),
                article.getUrl()
        );
    }
}
