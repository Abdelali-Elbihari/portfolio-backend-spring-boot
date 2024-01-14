package com.abdelalielbihari.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.util.UrlCache;
import com.abdelalielbihari.portfolio.util.UrlCache.CachedUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class AboutServiceTest {

  @MockBean
  private AboutRepository aboutRepository;

  @Mock
  private ImageService imageService;

  private AboutService aboutService;

  @Mock
  private RedisTemplate<String, CachedUrl> redisTemplate;

  @BeforeEach
  void setup() throws IOException {
    openMocks(this);
    when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
    when(redisTemplate.opsForValue().get(any())).thenReturn(new CachedUrl("presignedUrl"));
    when(imageService.uploadImage(any(MultipartFile.class))).thenReturn("uploadedImageUrl");
    aboutService = new AboutServiceImpl(aboutRepository, imageService, new UrlCache(redisTemplate, imageService));
  }

  @Test
  void testGetOneAbout() {
    // Arrange
    String aboutId = "1";
    About about = About.builder().id(aboutId).build();

    when(aboutRepository.findById(aboutId)).thenReturn(Optional.of(about));

    // Act
    Optional<About> result = aboutService.getAbout(aboutId);

    // Assert
    assertEquals(about, result.orElseThrow());
    verify(redisTemplate, times(3)).opsForValue();
  }

  @Test
  void testGetAllAbouts() {
    // Arrange
    About about1 = About.builder().id("1").build();
    About about2 = About.builder().id("2").build();
    List<About> aboutList = Arrays.asList(about1, about2);

    when(aboutRepository.findAll()).thenReturn(aboutList);

    // Act
    List<About> result = aboutService.getAllAbouts();

    // Assert
    assertEquals(2, result.size());
    verify(redisTemplate, times(5)).opsForValue();
  }

  @Test
  void testAddAbout() throws IOException {
    // Arrange
    MultipartFile image = mock(MultipartFile.class);
    String imageUrl = "uploadedImageUrl";
    About about = About.builder().build();

    when(imageService.uploadImage(image)).thenReturn(imageUrl);
    when(aboutRepository.save(about)).thenReturn(about);

    // Act
    About result = aboutService.addAbout(about, image);

    // Assert
    assertEquals(about, result);
    verify(redisTemplate, times(3)).opsForValue();
  }

  @Test
  void testUpdateAbout() throws IOException {
    // Arrange
    String aboutId = "1";
    MultipartFile image = mock(MultipartFile.class);
    About existingAbout = About.builder().id(aboutId).build();

    when(aboutRepository.findById(aboutId)).thenReturn(Optional.of(existingAbout));
    when(aboutRepository.save(any(About.class))).thenReturn(existingAbout);
    // Act
    Optional<About> result = aboutService.updateAbout(aboutId, existingAbout, image);

    // Assert
    assertEquals(existingAbout, result.orElseThrow());
    verify(redisTemplate, times(3)).opsForValue();
  }

  @Test
  void testDeleteAbout() {
    // Arrange
    String aboutId = "1";
    About existingAbout = About.builder().id(aboutId).build();
    when(aboutRepository.findById(aboutId)).thenReturn(Optional.of(existingAbout));

    // Act
    aboutService.deleteAbout(aboutId);

    // Assert
    verify(aboutRepository, times(1)).deleteById(aboutId);
  }
}
