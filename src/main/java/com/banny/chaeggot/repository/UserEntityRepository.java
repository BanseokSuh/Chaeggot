package com.banny.chaeggot.repository;

import com.banny.chaeggot.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);

    Optional<UserEntity> findByUserIdx(Long userIdx);
}
