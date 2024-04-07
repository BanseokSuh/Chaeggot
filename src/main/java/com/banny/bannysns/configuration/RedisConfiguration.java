package com.banny.bannysns.configuration;

import com.banny.bannysns.model.User;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisURI redisURI = RedisURI.create("redis://localhost"); // TODO: 추후 환경 변수로 변경
        org.springframework.data.redis.connection.RedisConfiguration redisConfiguration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfiguration);
        factory.afterPropertiesSet();

        return factory;
    }

    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory()); // Redis 서버 정보 셋팅
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Key를 String으로 사용했기 때문에 해당 serializer 설정
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class)); // Value를 User로 사용했기 때문에 해당 serializer 설정

        return redisTemplate;
    }

}
