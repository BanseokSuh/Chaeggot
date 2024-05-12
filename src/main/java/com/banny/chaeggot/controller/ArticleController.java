package com.banny.chaeggot.controller;

import com.banny.chaeggot.controller.request.ArticleCreateRequest;
import com.banny.chaeggot.controller.response.ArticleCreateResponse;
import com.banny.chaeggot.controller.response.Response;
import com.banny.chaeggot.model.Article;
import com.banny.chaeggot.model.User;
import com.banny.chaeggot.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Response<ArticleCreateResponse> createArticle(@RequestBody ArticleCreateRequest request, Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        Article article = articleService.createArticle(request.getTitle(), request.getUrl(), user.getUserIdx());
        return Response.success(ArticleCreateResponse.of(article));
    }

}
