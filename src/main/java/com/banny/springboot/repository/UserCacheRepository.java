package com.banny.springboot.repository;

import com.banny.springboot.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserCacheRepository {

    private final RedisTemplate<String, User> redisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUserId());
        log.info("Set User into Redis {}, {}", key, user);
        redisTemplate.opsForValue().set(key, user, USER_CACHE_TTL); // TODO:
    }

    public Optional<User> getUser(String userId) {
        String key = getKey(userId);
        User user = redisTemplate.opsForValue().get(key);
        log.info("Get data from Redis {}, {}", key, user);
        return Optional.ofNullable(user);
    }

    private String getKey(String userId) {
        return "USER:" + userId;
    }
}
