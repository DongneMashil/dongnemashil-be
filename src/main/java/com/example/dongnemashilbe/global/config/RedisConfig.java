//package com.example.dongnemashilbe.global.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//@Configuration
//@RequiredArgsConstructor
//
//public class RedisConfig {
//
//    private final RedisProperties redisProperties;
//
//@Bean
//public RedisConnectionFactory redisConnectionFactory() {
//    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//    redisStandaloneConfiguration.setHostName(redisProperties.getHost());
//    redisStandaloneConfiguration.setPort(redisProperties.getPort());
//    redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
//
//    return new LettuceConnectionFactory(redisStandaloneConfiguration);
//}
//
//    @Bean
//    public RedisTemplate<String, String> redisTemplate() {
//        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(1));  // 10분
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfig)
//                .build();
//    }
//}
