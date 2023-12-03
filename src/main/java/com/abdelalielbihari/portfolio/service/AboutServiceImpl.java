package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.model.AboutDto;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.util.AboutMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class AboutServiceImpl implements AboutService {

  AboutRepository aboutRepository;
  AboutMapper aboutMapper;
  private final ImageService imageService;

  @Override
  public Optional<AboutDto> getOneAbout(String id) {
    return aboutRepository.findById(id).map(value -> aboutMapper.toAboutDto(value));
  }

  @Override
  public List<AboutDto> getAllAbouts() {
    return aboutMapper.toAboutDtoList(aboutRepository.findAll());
  }

  @Override
  public AboutDto addAbout(AboutDto aboutDto, MultipartFile image) throws IOException {
    String imageUrl = imageService.uploadImage(image);
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
      String imageUrl = imageService.uploadImage(image);
      updatedAbout.setImageUrl(imageUrl);

      aboutRepository.save(updatedAbout);
    }

    return newAbout.map(about -> aboutMapper.toAboutDto(about));
  }

  @Override
  public void deleteAbout(String id) {
    aboutRepository.findById(id).ifPresent(value -> aboutRepository.delete(value));
  }
}
