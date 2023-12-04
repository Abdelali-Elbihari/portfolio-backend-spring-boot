package com.abdelalielbihari.portfolio.util;

import com.abdelalielbihari.portfolio.service.ImageService;
import lombok.Getter;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
public class UrlCache {

  private final Map<String, CachedUrl> cache = new HashMap<>();
  private final ImageService imageService;

  public UrlCache(ImageService imageService) {
    this.imageService = imageService;
  }

  public String getOrGeneratePresignedUrl(String imageUrl) {
    CachedUrl existingValue = cache.get(imageUrl);

    if (existingValue == null || existingValue.isExpired()) {
      generateAndCacheUrl(imageUrl);
    }

    return cache.get(imageUrl).getUrl();
  }

  private void generateAndCacheUrl(String imageUrl) {
    String newUrl = imageService.getPresignedImageUrl(imageUrl);
    CachedUrl newCachedUrl = new CachedUrl(newUrl);
    cache.put(imageUrl, newCachedUrl);
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
