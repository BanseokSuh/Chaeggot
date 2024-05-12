package com.banny.chaeggot.controller;

import com.banny.chaeggot.controller.request.ArticleCreateRequest;
import com.banny.chaeggot.controller.response.ArticleCreateResponse;
import com.banny.chaeggot.controller.response.ArticleResponse;
import com.banny.chaeggot.controller.response.Response;
import com.banny.chaeggot.model.Article;
import com.banny.chaeggot.model.User;
import com.banny.chaeggot.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Article article = articleService.createArticle(request.getTitle(), request.getUrl(), user.getUserIdx());
        return Response.success(ArticleCreateResponse.from(article));
    }

    /**
     * Delete an article
     *
     * @param articleIdx
     * @param authentication
     * @return
     */
    @DeleteMapping("/{articleIdx}")
    public Response<Void> deleteArticle(@PathVariable(name = "articleIdx") Long articleIdx, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        articleService.deleteArticle(articleIdx, user.getUserIdx());
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

}
