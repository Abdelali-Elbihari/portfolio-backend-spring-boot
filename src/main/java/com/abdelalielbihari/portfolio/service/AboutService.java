package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.About;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AboutService {

  Optional<About> getAbout(String id);

  List<About> getAllAbouts();

  About addAbout(About about, MultipartFile image) throws IOException;

  Optional<About> updateAbout(String id, About about, MultipartFile image) throws IOException;

  void deleteAbout(String id);
}
