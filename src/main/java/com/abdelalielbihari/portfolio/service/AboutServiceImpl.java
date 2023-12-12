package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.dto.AboutDto;
import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.util.AboutMapper;
import com.abdelalielbihari.portfolio.util.UrlCache;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AboutServiceImpl implements AboutService {

  private final AboutRepository aboutRepository;
  private final ImageService imageService;
  private final UrlCache urlCache;


  @Override
  public Optional<About> getAbout(String id) {
    return aboutRepository.findById(id).map(about -> {
      about.setImageUrl(urlCache.getOrGeneratePresignedUrl(about.getImageUrl()));
      return about;
    });
  }

  @Override
  public List<About> getAllAbouts() {
    List<About> aboutList = aboutRepository.findAll();
    aboutList.forEach(about -> about.setImageUrl(urlCache.getOrGeneratePresignedUrl(about.getImageUrl())));
    return aboutList;
  }

  @Override
  public About addAbout(About about, MultipartFile image) throws IOException {
    return handleSaveWitImage(image, about);
  }

  @Override
  public Optional<About> updateAbout(String id, About aboutDto, MultipartFile image) throws IOException {
    Optional<About> newAbout = aboutRepository.findById(id);

    if (newAbout.isPresent()) {
      About updatedAbout = newAbout.get();
      updatedAbout.setTitle(aboutDto.getTitle());
      updatedAbout.setDescription(aboutDto.getDescription());
      return Optional.of(handleSaveWitImage(image, updatedAbout));
    }

    return Optional.empty();
  }

  private About handleSaveWitImage(MultipartFile image, About about) throws IOException {
    // todo replace image instead
    String imageUrl = imageService.uploadImage(image);
    about.setImageUrl(imageUrl);

    //Save About entity with normal url
    About newAbout = aboutRepository.save(about);

    //get Or Generate Presigned Url
    newAbout.setImageUrl(urlCache.getOrGeneratePresignedUrl(newAbout.getImageUrl()));
    return newAbout;
  }

  @Override
  public void deleteAbout(String id) {
    aboutRepository.findById(id).ifPresent(aboutRepository::delete);
    //todo delete image
  }
}
