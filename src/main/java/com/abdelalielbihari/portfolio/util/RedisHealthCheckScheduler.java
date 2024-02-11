package com.abdelalielbihari.portfolio.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class RedisHealthCheckScheduler {

  private final RedisTemplate<String, String> redisTemplate;

  @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // runs every 24 hours
  public void testRedisConnection() {
    log.info("Running Redis health check");

    redisTemplate.opsForValue().set("testKey", "testValue");

    String retrievedValue = redisTemplate.opsForValue().get("testKey");
    if (!"testValue".equals(retrievedValue)) {
      log.error("Redis connection test failed");
    } else {
      log.info("Redis health check passed");
    }

    redisTemplate.delete("testKey");
  }
}