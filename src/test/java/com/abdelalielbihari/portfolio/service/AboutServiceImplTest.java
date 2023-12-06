package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.model.AboutDto;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.util.AboutMapper;
import com.abdelalielbihari.portfolio.util.UrlCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AboutServiceImplTest {

  @Mock
  private AboutRepository aboutRepository;

  @Mock
  private AboutMapper aboutMapper;

  @Mock
  private ImageService imageService;

  @Mock
  private UrlCache urlCache;

  @InjectMocks
  private AboutServiceImpl aboutService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetOneAbout() {
    // Arrange
    String aboutId = "1";
    About about = About.builder().id(aboutId).build();
    AboutDto aboutDto = AboutDto.builder().id(aboutId).build();

    when(aboutRepository.findById(aboutId)).thenReturn(Optional.of(about));
    when(aboutMapper.toAboutDto(about)).thenReturn(aboutDto);
    when(urlCache.getOrGeneratePresignedUrl(nullable(String.class))).thenReturn("presignedUrl");

    // Act
    Optional<AboutDto> result = aboutService.getOneAbout(aboutId);

    // Assert
    assertEquals(aboutDto, result.orElseThrow());
    verify(urlCache, times(1)).getOrGeneratePresignedUrl(nullable(String.class));
  }

  @Test
  void testGetAllAbouts() {
    // Arrange
    About about1 = About.builder().id("1").build();
    About about2 = About.builder().id("2").build();
    List<About> aboutList = Arrays.asList(about1, about2);

    when(aboutRepository.findAll()).thenReturn(aboutList);
    when(aboutMapper.toAboutDtoList(aboutList)).thenReturn(Arrays.asList(
        AboutDto.builder().id("1").build(),
        AboutDto.builder().id("2").build()
    ));
    when(urlCache.getOrGeneratePresignedUrl(nullable(String.class))).thenReturn("presignedUrl");

    // Act
    List<AboutDto> result = aboutService.getAllAbouts();

    // Assert
    assertEquals(2, result.size());
    verify(urlCache, times(2)).getOrGeneratePresignedUrl(nullable(String.class));
  }

  @Test
  void testAddAbout() throws IOException {
    // Arrange
    AboutDto aboutDto = AboutDto.builder().build();
    MultipartFile image = mock(MultipartFile.class);
    String imageUrl = "uploadedImageUrl";
    About about = About.builder().build();

    when(imageService.uploadImage(image)).thenReturn(imageUrl);
    when(urlCache.getOrGeneratePresignedUrl(imageUrl)).thenReturn("presignedUrl");
    when(aboutMapper.toAbout(aboutDto)).thenReturn(about);
    when(aboutRepository.save(about)).thenReturn(about);
    when(aboutMapper.toAboutDto(about)).thenReturn(aboutDto);

    // Act
    AboutDto result = aboutService.addAbout(aboutDto, image);

    // Assert
    assertEquals(aboutDto, result);
    verify(urlCache, times(1)).getOrGeneratePresignedUrl(nullable(String.class));
  }

  @Test
  void testUpdateAbout() throws IOException {
    // Arrange
    String aboutId = "1";
    AboutDto aboutDto = AboutDto.builder().id(aboutId).build();
    MultipartFile image = mock(MultipartFile.class);
    String imageUrl = "uploadedImageUrl";
    About existingAbout = About.builder().id(aboutId).build();

    when(imageService.uploadImage(image)).thenReturn(imageUrl);
    when(urlCache.getOrGeneratePresignedUrl(imageUrl)).thenReturn("presignedUrl");
    when(aboutRepository.findById(aboutId)).thenReturn(Optional.of(existingAbout));
    when(aboutRepository.save(any(About.class))).thenReturn(existingAbout);
    when(aboutMapper.toAboutDto(existingAbout)).thenReturn(aboutDto);
    // Act
    Optional<AboutDto> result = aboutService.updateAbout(aboutId, aboutDto, image);

    // Assert
    assertEquals(aboutDto, result.orElseThrow());
    verify(urlCache, times(1)).getOrGeneratePresignedUrl(nullable(String.class));
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
    verify(aboutRepository, times(1)).findById(aboutId);
    verify(aboutRepository, times(1)).delete(any());
  }
}
