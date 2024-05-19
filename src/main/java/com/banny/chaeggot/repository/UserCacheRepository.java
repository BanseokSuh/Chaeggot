package com.banny.chaeggot.repository;

import com.banny.chaeggot.model.User;
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
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3); // 3 days

    /**
     * Set User into Redis
     * The param secretKey is just a string, so it needs to be converted to a key.
     *
     * @param user
     */
    public void setUser(User user) {
        String key = getKey(user.getLoginId());
        redisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
        log.info("Set User into Redis {}, {}", key, user);
    }

    /**
     * Get User from Redis
     *
     * @param loginId
     * @return User
     */
    public Optional<User> getUser(String loginId) {
        String key = getKey(loginId);
        User user = redisTemplate.opsForValue().get(key);
        log.info("Get data from Redis {}, {}", key, user);
        return Optional.ofNullable(user);
    }

    /**
     * Get redis key for user
     *
     * @param loginId
     * @return
     */
    private String getKey(String loginId) {
        return "USER:" + loginId;
    }
}
