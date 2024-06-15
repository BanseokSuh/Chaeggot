package com.banny.chaeggot.service;

import com.banny.chaeggot.exception.ApplicationException;
import com.banny.chaeggot.exception.ErrorCode;
import com.banny.chaeggot.entity.Article;
import com.banny.chaeggot.entity.User;
import com.banny.chaeggot.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;

    /**
     * Create an article
     *
     * @param title
     * @param url
     * @param userId
     * @return
     */
    public Article createArticle(String title, String url, Long userId) {
        User user = userService.getUserEntityOrException(userId);
        return articleRepository.save(Article.of(title, url, user));
    }

    /**
     * Delete an article
     *
     * @param articleId
     * @param userId
     */
    public void deleteArticle(Long articleId, Long userId) {
        User user = userService.getUserEntityOrException(userId);
        Article articleEntity = getArticleEntityOrException(articleId);

        if (!articleEntity.getUser().equals(user)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("userId[%s] has no permission with articleId[%s]", userId, articleId));
        }

        articleEntity.delete();

        articleRepository.saveAndFlush(articleEntity);
    }

    /**
     * Get articles
     *
     * @param pageable
     * @return
     */
    public Page<Article> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    /**
     * Get an article
     *
     * @param articleId
     * @return
     */
    public Article getArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND, String.format("ArticleId[%s] not found", articleId)));
    }

    // =================================================================================================================

    /**
     * Get an article or exception
     *
     * @param articleId
     * @return
     */
    public Article getArticleEntityOrException(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND, String.format("articleId[%s] not found", articleId)));
    }
}

