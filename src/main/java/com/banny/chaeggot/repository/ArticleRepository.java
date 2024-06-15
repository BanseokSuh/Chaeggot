package com.banny.chaeggot.repository;

import com.banny.chaeggot.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
