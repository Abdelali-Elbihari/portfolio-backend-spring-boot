package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.model.AboutDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface AboutService {
  Optional<AboutDto> getOneAbout(String id);

  List<AboutDto> getAllAbouts();

  AboutDto addAbout(AboutDto about, MultipartFile image) throws IOException;

   Optional<AboutDto> updateAbout(String id, AboutDto aboutDto, MultipartFile image) throws IOException;

  void deleteAbout(String id);
}
