package com.banny.chaeggot.repository;

import com.banny.chaeggot.model.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {
}
