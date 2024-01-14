package com.abdelalielbihari.portfolio.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.dto.AboutDto;
import com.abdelalielbihari.portfolio.repository.AboutRepository;
import com.abdelalielbihari.portfolio.service.AboutServiceImpl;
import com.abdelalielbihari.portfolio.service.ImageService;
import com.abdelalielbihari.portfolio.util.AboutMapper;
import com.abdelalielbihari.portfolio.util.UrlCache;
import com.abdelalielbihari.portfolio.util.UrlCache.CachedUrl;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class AboutControllerTest {

  @Autowired
  private AboutRepository aboutRepository;
  @Autowired
  private AboutMapper aboutMapper;
  private AboutServiceImpl aboutService;

  @Mock
  private ImageService imageService;

  @Mock
  private RedisTemplate<String, CachedUrl> redisTemplate;

  private MockMvc mockMvc;

  @Container
  private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.12");

  AboutControllerTest() {
  }


  @DynamicPropertySource
  static void setMongoProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @BeforeEach
  void setUp() throws IOException {
    openMocks(this);
    when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
    when(redisTemplate.opsForValue().get(any())).thenReturn(new CachedUrl("presignedUrl"));
    when(imageService.uploadImage(any(MockMultipartFile.class))).thenReturn("mockedImageUrl");
    when(imageService.getPresignedImageUrl(any())).thenReturn("presignedUrl");
    aboutService = new AboutServiceImpl(aboutRepository, imageService, new UrlCache(redisTemplate, imageService));
    mockMvc = MockMvcBuilders.standaloneSetup(new AboutController(aboutService, aboutMapper)).build();
  }

  @Test
  void testGetOneAbout() throws Exception {
    // Arrange

    AboutDto aboutDto = AboutDto.builder().build();
    MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

    About savedAbout = aboutService.addAbout(aboutMapper.toAbout(aboutDto), image);

    // Perform the request and assert the response
    mockMvc.perform(MockMvcRequestBuilders.get("/api/about/{id}", savedAbout.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(savedAbout.getId()))
        .andExpect(jsonPath("$.title").value(savedAbout.getTitle()))
        .andExpect(jsonPath("$.description").value(savedAbout.getDescription()))
        .andExpect(jsonPath("$.imgUrl").value(savedAbout.getImgUrl()));
  }

  @Test
  void testGetAllAbouts() throws Exception {
    // Arrange
    aboutService.addAbout(About.builder().build(),
        new MockMultipartFile("image1", "test1.jpg", "image/jpeg", new byte[0]));
    aboutService.addAbout(About.builder().build(),
        new MockMultipartFile("image2", "test2.jpg", "image/jpeg", new byte[0]));

    // Perform the request and assert the response
    mockMvc.perform(MockMvcRequestBuilders.get("/api/about"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  void testAddAbout() throws Exception {
    // Arrange
    AboutDto aboutDto = AboutDto.builder().title("title1").description("desc1").build();
    MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

    // Perform the request and assert the response
    mockMvc.perform(multipart("/api/about")
            .file(image)
            .flashAttr("aboutDto", aboutDto)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.title").value(aboutDto.getTitle()))
        .andExpect(jsonPath("$.description").value(aboutDto.getDescription()))
        .andExpect(jsonPath("$.imgUrl").value("presignedUrl"));
  }

  @Test
  void testUpdateAbout() throws Exception {
    // Arrange
    About about = About.builder().build();
    MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

    About existingAbout = aboutService.addAbout(about, image);

    AboutDto updatedDto = AboutDto.builder().id(existingAbout.getId()).title("Updated Title")
        .description("Updated Description").build();
    MockMultipartFile updatedImage = new MockMultipartFile("updatedImage", "updatedImage.jpg", "image/jpeg",
        new byte[0]);

    // Perform the request and assert the response
    mockMvc.perform(multipart(HttpMethod.PUT, "/api/about/{id}", updatedDto.getId())
            .file("image", updatedImage.getBytes())
            .flashAttr("aboutDto", updatedDto)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(updatedDto.getId()))
        .andExpect(jsonPath("$.title").value(updatedDto.getTitle()))
        .andExpect(jsonPath("$.description").value(updatedDto.getDescription()))
        .andExpect(jsonPath("$.imgUrl").value("presignedUrl"));
  }

  @Test
  void testUpdateAboutNotFound() throws Exception {
    // Arrange
    AboutDto aboutDto = AboutDto.builder().id("nonexistent").title("Updated Title").description("Updated Description")
        .build();
    MockMultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", new byte[0]);

    // Perform the request and assert the response
    mockMvc.perform(multipart(HttpMethod.PUT, "/api/about/{id}", aboutDto.getId())
            .file("image", image.getBytes())
            .flashAttr("aboutDto", aboutDto)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteAbout() throws Exception {
    // Arrange
    About savedAbout = aboutService.addAbout(About.builder().build(),
        new MockMultipartFile("image1", "test1.jpg", "image/jpeg", new byte[0]));

    // Perform the request and assert the response
    mockMvc.perform(delete("/api/about/{id}", savedAbout.getId()))
        .andExpect(status().isOk())
        .andExpect(content().string("About deleted successfully"));
  }
}
