package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.model.AboutDto;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.util.AboutMapper;
import com.abdelalielbihari.portfolio.util.UrlCache;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class AboutServiceImpl implements AboutService {

  private final AboutRepository aboutRepository;
  private final AboutMapper aboutMapper;
  private final ImageService imageService;
  private final UrlCache urlCache;

  public AboutServiceImpl(AboutRepository aboutRepository, AboutMapper aboutMapper, ImageService imageService, UrlCache urlCache) {
    this.aboutRepository = aboutRepository;
    this.aboutMapper = aboutMapper;
    this.imageService = imageService;
    this.urlCache = urlCache;
  }

  @Override
  public Optional<AboutDto> getOneAbout(String id) {
    return aboutRepository.findById(id).map(about -> {
      AboutDto aboutDto = aboutMapper.toAboutDto(about);
      aboutDto.setImageUrl(urlCache.getOrGeneratePresignedUrl(aboutDto.getImageUrl()));
      return aboutDto;
    });
  }

  @Override
  public List<AboutDto> getAllAbouts() {
    List<AboutDto> aboutDtoList = aboutMapper.toAboutDtoList(aboutRepository.findAll());
    aboutDtoList.forEach(aboutDto -> {
      aboutDto.setImageUrl(urlCache.getOrGeneratePresignedUrl(aboutDto.getImageUrl()));
    });
    return aboutDtoList;
  }

  @Override
  public AboutDto addAbout(AboutDto aboutDto, MultipartFile image) throws IOException {
    String imageUrl = urlCache.getOrGeneratePresignedUrl(imageService.uploadImage(image));
    aboutDto.setImageUrl(imageUrl);
    About about = aboutRepository.save(aboutMapper.toAbout(aboutDto));
    return aboutMapper.toAboutDto(about);
  }

  @Override
  public Optional<AboutDto> updateAbout(String id, AboutDto aboutDto, MultipartFile image) throws IOException {
    Optional<About> newAbout = aboutRepository.findById(id);

    if (newAbout.isPresent()) {
      About updatedAbout = newAbout.get();
      updatedAbout.setTitle(aboutDto.getTitle());
      updatedAbout.setDescription(aboutDto.getDescription());

      // todo replace image instead
      String imageUrl = urlCache.getOrGeneratePresignedUrl(imageService.uploadImage(image));
      updatedAbout.setImageUrl(imageUrl);

      aboutRepository.save(updatedAbout);
    }

    return newAbout.map(aboutMapper::toAboutDto);
  }

  @Override
  public void deleteAbout(String id) {
    aboutRepository.findById(id).ifPresent(value -> aboutRepository.delete(value));
  }
}
