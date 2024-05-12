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
     * @param userIdx
     * @return
     */
    public Article createArticle(String title, String url, Long userIdx) {
        UserEntity userEntity = userService.getUserEntityOrException(userIdx);
        ArticleEntity articleEntity = articleEntityRepository.save(ArticleEntity.of(title, url, userEntity));
        return Article.fromEntity(articleEntity);
    }

    /**
     * Delete an article
     *
     * @param articleIdx
     * @param userIdx
     */
    public void deleteArticle(Long articleIdx, Long userIdx) {
        UserEntity userEntity = userService.getUserEntityOrException(userIdx);
        ArticleEntity articleEntity = getArticleEntityOrException(articleIdx);

        if (!articleEntity.getUser().equals(userEntity)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION, String.format("UserIdx[%s] has no permission with ArticleIdx[%s]", userIdx, articleIdx));
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

    // =================================================================================================================

    /**
     * Get an article or exception
     *
     * @param articleIdx
     * @return
     */
    public ArticleEntity getArticleEntityOrException(Long articleIdx) {
        return articleEntityRepository.findById(articleIdx).orElseThrow(() ->
                new ApplicationException(ErrorCode.ARTICLE_NOT_FOUND, String.format("ArticleIdx[%s] not found", articleIdx)));
    }
}

