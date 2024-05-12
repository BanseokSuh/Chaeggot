package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.model.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleCreateResponse {
    private Long articleIdx;

    public static ArticleCreateResponse of(Article article) {
        return new ArticleCreateResponse(article.getArticleIdx());
    }
}
