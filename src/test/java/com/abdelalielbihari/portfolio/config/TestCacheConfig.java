package com.abdelalielbihari.portfolio.config;

import com.abdelalielbihari.portfolio.util.UrlCache;
import com.abdelalielbihari.portfolio.util.UrlCache.CachedUrl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

//@Configuration
//@EnableCaching
//@Profile("test")
public class TestCacheConfig {
  @Bean
  public JedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
    return new JedisConnectionFactory(config);
  }

  @Bean(name = "testRedisTemplate")
  public RedisTemplate<String, CachedUrl> redisTemplate( RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, UrlCache.CachedUrl> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
