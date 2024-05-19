package com.banny.chaeggot.service;

import com.banny.chaeggot.exception.ApplicationException;
import com.banny.chaeggot.exception.ErrorCode;
import com.banny.chaeggot.model.Article;
import com.banny.chaeggot.model.entity.ArticleEntity;
import com.banny.chaeggot.model.entity.UserEntity;
import com.banny.chaeggot.repository.ArticleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final UserService userService;

    /**
     * Create an article
     *
     * @param title
     * @param url
     * @param id
     * @return
     */
    public Article createArticle(String title, String url, Long id) {
        UserEntity userEntity = userService.getUserEntityOrException(id);
        ArticleEntity articleEntity = articleEntityRepository.save(ArticleEntity.of(title, url, userEntity));
        return Article.fromEntity(articleEntity);
    }

    /**
     * Delete an article
     *
     * @param articleId
     * @param userId
     */
    public void deleteArticle(Long articleId, Long userId) {
        UserEntity userEntity = userService.getUserEntityOrException(userId);
        ArticleEntity articleEntity = getArticleEntityOrException(articleId);

        if (!articleEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("userId[%s] has no permission with articleId[%s]", userId, articleId));
        }

        articleEntity.delete();

        articleEntityRepository.saveAndFlush(articleEntity);
    }

    /**
     * Get articles
     *
     * @param pageable
     * @return
     */
    public Page<Article> getArticles(Pageable pageable) {
        return articleEntityRepository.findAll(pageable).map(Article::fromEntity);
    }

    /**
     * Get an article
     *
     * @param id
     * @return
     */
    public Article getArticle(Long id) {
        return articleEntityRepository.findById(id).map(Article::fromEntity).orElseThrow(() ->
                new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND, String.format("ArticleIdx[%s] not found", id)));
    }

    // =================================================================================================================

    /**
     * Get an article or exception
     *
     * @param articleId
     * @return
     */
    public ArticleEntity getArticleEntityOrException(Long articleId) {
        return articleEntityRepository.findById(articleId).orElseThrow(() ->
                new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND, String.format("articleId[%s] not found", articleId)));
    }
}

