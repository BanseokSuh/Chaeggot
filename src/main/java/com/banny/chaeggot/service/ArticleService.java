package com.banny.chaeggot.service;

import com.banny.chaeggot.model.Article;
import com.banny.chaeggot.model.entity.ArticleEntity;
import com.banny.chaeggot.model.entity.UserEntity;
import com.banny.chaeggot.repository.ArticleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final UserService userService;

    public Article createArticle(String title, String url, Long userIdx) {
        UserEntity userEntity = userService.getUserEntityOrException(userIdx);
        ArticleEntity articleEntity = articleEntityRepository.save(ArticleEntity.of(title, url, userEntity));
        return Article.fromEntity(articleEntity);
    }
}

