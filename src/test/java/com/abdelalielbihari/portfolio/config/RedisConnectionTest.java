package com.abdelalielbihari.portfolio.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisConnectionTest {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Test
  public void testRedisConnection() {
    redisTemplate.opsForValue().set("testKey", "testValue");

    String retrievedValue = redisTemplate.opsForValue().get("testKey");
    assertEquals("testValue", retrievedValue, "Redis connection test failed");

    redisTemplate.delete("testKey");
  }
}
