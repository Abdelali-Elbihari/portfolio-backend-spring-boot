package com.abdelalielbihari.portfolio.config;

import com.abdelalielbihari.portfolio.util.UrlCache;
import com.abdelalielbihari.portfolio.util.UrlCache.CachedUrl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class CacheConfig {

  @Bean
  public RedisTemplate<String, CachedUrl> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, UrlCache.CachedUrl> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
