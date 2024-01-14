package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.util.UrlCache;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {

  private final AboutRepository aboutRepository;
  private final ImageService imageService;
  private final UrlCache urlCache;


  @Override
  @Cacheable(value = "aboutCache", key = "#id")
  public Optional<About> getAbout(String id) {
    return aboutRepository.findById(id).map(about -> {
      about.setImgUrl(urlCache.getOrGeneratePresignedImgUrl(about.getImgUrl()));
      return about;
    });
  }

  @Override
  @Cacheable(value = "aboutCache", key = "'allAbouts'")
  public List<About> getAllAbouts() {
    List<About> aboutList = aboutRepository.findAll();
    aboutList.forEach(about -> about.setImgUrl(urlCache.getOrGeneratePresignedImgUrl(about.getImgUrl())));
    return aboutList;
  }

  @Override
  public About addAbout(About about, MultipartFile image) throws IOException {
    return handleSaveWithImage(about, image);
  }

  @Override
  public Optional<About> updateAbout(String id, About about, MultipartFile image) throws IOException {
    Optional<About> currentAbout = aboutRepository.findById(id);

    if (currentAbout.isPresent()) {
      evictAboutCache(currentAbout.get());
      About updatedAbout = currentAbout.get();
      updatedAbout.setTitle(about.getTitle());
      updatedAbout.setDescription(about.getDescription());
      return Optional.of(handleSaveWithImage(updatedAbout, image));
    }

    return currentAbout;
  }

  private About handleSaveWithImage(About about, MultipartFile image) throws IOException {
    // todo replace image instead
    String imageUrl = imageService.uploadImage(image);
    about.setImgUrl(imageUrl);

    //Save About entity with normal url
    About newAbout = aboutRepository.save(about);

    //get Or Generate Presigned Url
    newAbout.setImgUrl(urlCache.getOrGeneratePresignedImgUrl(newAbout.getImgUrl()));
    return newAbout;
  }

  @Override
  @CacheEvict(value = "aboutCache", key = "#id")
  public void deleteAbout(String id) {
    aboutRepository.deleteById(id);
    //todo delete image
  }

  @CacheEvict(value = "aboutCache", key = "#about.id")
  public void evictAboutCache(About about) {
    urlCache.evictCacheForImage(about.getImgUrl());
  }
}
