package com.abdelalielbihari.portfolio.util;

import com.abdelalielbihari.portfolio.service.ImageService;
import lombok.Getter;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class UrlCache {

  private final ConcurrentMap<String, CachedUrl> cache = new ConcurrentHashMap<>();
  private final ImageService imageService;

  public UrlCache(ImageService imageService, S3Presigner s3Presigner) {
    this.imageService = imageService;
  }

  public String getOrGeneratePresignedUrl(String imageUrl) {
    return cache.compute(imageUrl, (key, existingValue) -> existingValue == null || existingValue.isExpired()
        ? generateAndCacheUrl(key)
        : existingValue).getUrl();
  }

  private CachedUrl generateAndCacheUrl(String imageUrl) {
    String newUrl = imageService.getPresignedImageUrl(imageUrl);
    CachedUrl newCachedUrl = new CachedUrl(newUrl);
    cache.put(imageUrl, newCachedUrl);
    return newCachedUrl;
  }

  private static class CachedUrl {

    @Getter
    private final String url;
    private final long expirationTimeMillis;

    CachedUrl(String url) {
      this.url = url;
      this.expirationTimeMillis = System.currentTimeMillis() + Duration.ofMinutes(120).toMillis(); // Adjust as needed
    }

    boolean isExpired() {
      return System.currentTimeMillis() >= expirationTimeMillis;
    }
  }
}
