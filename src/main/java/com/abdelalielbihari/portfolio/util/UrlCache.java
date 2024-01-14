package com.abdelalielbihari.portfolio.util;

import com.abdelalielbihari.portfolio.service.ImageService;
import java.io.Serializable;
import java.time.Duration;
import java.util.Optional;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlCache {

  private static final String IMG_CACHE_KEY_PREFIX = "presignedImgUrl:";

  private final RedisTemplate<String, CachedUrl> redisTemplate;
  private final ImageService imageService;

  public UrlCache(RedisTemplate<String, CachedUrl> redisTemplate, ImageService imageService) {
    this.redisTemplate = redisTemplate;
    this.imageService = imageService;
  }


  public String getOrGeneratePresignedImgUrl(String imageUrl) {
    CachedUrl existingValue = redisTemplate.opsForValue().get(getImgCacheKey(imageUrl));

    if (existingValue == null || existingValue.getUrl() == null || existingValue.isExpired()) {
      generateAndCacheImgUrl(imageUrl);
    }

    return Optional.ofNullable(redisTemplate.opsForValue().get(getImgCacheKey(imageUrl)))
        .map(CachedUrl::getUrl)
        .orElse(imageUrl);
  }

  private void generateAndCacheImgUrl(String imageUrl) {
    String newUrl = imageService.getPresignedImageUrl(imageUrl);
    CachedUrl newCachedUrl = new CachedUrl(newUrl);
    redisTemplate.opsForValue().set(getImgCacheKey(imageUrl), newCachedUrl);
  }

  private String getImgCacheKey(String imageUrl) {
    return IMG_CACHE_KEY_PREFIX + imageUrl;
  }

  public void evictCacheForImage(String imageUrl) {
    redisTemplate.delete(getImgCacheKey(imageUrl));
  }

  public static class CachedUrl implements Serializable {

    @Getter
    private final String url;
    private final long expirationTimeMillis;

    public CachedUrl(String url) {
      this.url = url;
      this.expirationTimeMillis = System.currentTimeMillis() + Duration.ofMinutes(120).toMillis();
    }

    boolean isExpired() {
      return System.currentTimeMillis() >= expirationTimeMillis;
    }
  }
}
