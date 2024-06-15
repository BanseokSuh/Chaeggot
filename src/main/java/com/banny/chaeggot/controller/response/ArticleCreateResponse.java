package com.banny.chaeggot.controller.response;

import com.banny.chaeggot.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleCreateResponse {
    private Long id;

    public static ArticleCreateResponse from(Article article) {
        return new ArticleCreateResponse(article.getId());
    }
}
