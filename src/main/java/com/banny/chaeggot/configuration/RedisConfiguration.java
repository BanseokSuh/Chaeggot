package com.banny.chaeggot.configuration;

import com.banny.chaeggot.model.User;
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

    /**
     * Create a connection factory using the RedisProperties.
     *
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisURI redisURI = RedisURI.create(redisProperties.getHost(), redisProperties.getPort());
//        org.springframework.data.redis.connection.RedisConfiguration redisConfiguration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
        var redisConfiguration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    /**
     * Create a RedisTemplate for User.
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory()); // Set the connection factory
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Key is used as a String, so set the serializer accordingly
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class)); // Value is used as a String, so set the serializer accordingly

        return redisTemplate;
    }

}
