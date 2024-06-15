package com.banny.chaeggot.controller;

import com.banny.chaeggot.controller.request.ArticleCreateRequest;
import com.banny.chaeggot.controller.response.ArticleCreateResponse;
import com.banny.chaeggot.controller.response.ArticleResponse;
import com.banny.chaeggot.controller.response.Response;
import com.banny.chaeggot.entity.Article;
import com.banny.chaeggot.entity.User;
import com.banny.chaeggot.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    /**
     * Create an article
     *
     * @param request
     * @param authentication
     * @return
     */
    @PostMapping
    public Response<ArticleCreateResponse> createArticle(@RequestBody ArticleCreateRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Article article = articleService.createArticle(request.getTitle(), request.getUrl(), user.getId());
        return Response.success(ArticleCreateResponse.from(article));
    }

    /**
     * Delete an article
     *
     * @param articleId
     * @param authentication
     * @return
     */
    @DeleteMapping("/{articleId}")
    public Response<Void> deleteArticle(@PathVariable(name = "articleId") Long articleId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        articleService.deleteArticle(articleId, user.getId());
        return Response.success();
    }

    /**
     * Get articles
     *
     * @param pageable
     * @return
     */
    @GetMapping
    public Response<Page<ArticleResponse>> getArticles(Pageable pageable) {
        return Response.success(articleService.getArticles(pageable).map(ArticleResponse::from));
    }

    /**
     * Get an article
     *
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    public Response<ArticleResponse> getArticle(@PathVariable(name = "articleId") Long articleId) {
        return Response.success(ArticleResponse.from(articleService.getArticle(articleId)));
    }

}
