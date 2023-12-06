package com.abdelalielbihari.portfolio.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.abdelalielbihari.portfolio.model.AboutDto;
import com.abdelalielbihari.portfolio.service.AboutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest
@ComponentScan({"com.abdelalielbihari.portfolio.service", "com.abdelalielbihari.portfolio.config"})
class AboutControllerTest {

  @Mock
  private AboutService aboutService;

  @InjectMocks
  private AboutController aboutController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
//    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc = MockMvcBuilders.standaloneSetup(aboutController).build();
  }

//  @Test
  void testGetOneAbout() throws Exception {
    // Arrange
    String aboutId = "1";
    AboutDto aboutDto = AboutDto.builder().id(aboutId).build();
    Optional<AboutDto> aboutOptional = Optional.of(aboutDto);

    when(aboutService.getOneAbout(aboutId)).thenReturn(aboutOptional);

    // Act
    ResultActions resultActions = mockMvc.perform(get("/api/about/{id}", aboutId));

    // Assert
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(aboutId));

    verify(aboutService, times(1)).getOneAbout(aboutId);
  }

//  @Test
  void testGetAllAbouts() throws Exception {
    // Arrange
    AboutDto aboutDto1 = AboutDto.builder().id("1").build();
    AboutDto aboutDto2 = AboutDto.builder().id("2").build();
    List<AboutDto> aboutDtoList = Arrays.asList(aboutDto1, aboutDto2);
    when(aboutService.getAllAbouts()).thenReturn(aboutDtoList);

    // Act
    ResultActions resultActions = mockMvc.perform(get("/api/about"));

    // Assert
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value("1"))
        .andExpect(jsonPath("$[1].id").value("2"));

    verify(aboutService, times(1)).getAllAbouts();
  }

//  @Test
  void testAddAbout() throws Exception {
    // Arrange
    AboutDto aboutDto = AboutDto.builder().id("1").build();
    MockMultipartFile image = mock(MockMultipartFile.class);
    AboutDto savedAbout = AboutDto.builder().id("1").build();

    when(aboutService.addAbout(aboutDto, image)).thenReturn(savedAbout);

    // Initialize mockMvc with standaloneSetup to use only AboutController
    mockMvc = MockMvcBuilders.standaloneSetup(aboutController).build();

    // Act
    ResultActions resultActions = mockMvc.perform(
        multipart("/api/about")
            .file(image)
            .param("aboutDto", "{ \"title\": \"New Title\", \"description\": \"New Description\" }")
    );

    // Assert
    resultActions.andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"));

    verify(aboutService, times(1)).addAbout(aboutDto, image);
  }

//  @Test
  void testUpdateAbout() throws Exception {
    // Arrange
    String aboutId = "1";
    AboutDto aboutDto = AboutDto.builder().id(aboutId).build();
    MockMultipartFile image = mock(MockMultipartFile.class);
    Optional<AboutDto> aboutOptional = Optional.of(aboutDto);

    when(aboutService.updateAbout(aboutId, aboutDto, image)).thenReturn(aboutOptional);

    // Initialize mockMvc with standaloneSetup to use only AboutController
    mockMvc = MockMvcBuilders.standaloneSetup(aboutController).build();

    // Act
    ResultActions resultActions = mockMvc.perform(
        multipart("/api/about/{id}", aboutId)
            .file(image)
            .param("aboutDto", "{ \"title\": \"Updated Title\", \"description\": \"Updated Description\" }")
    );

    // Assert
    resultActions.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(aboutId));

    verify(aboutService, times(1)).updateAbout(aboutId, aboutDto, image);
  }

//  @Test
  void testUpdateAboutNotFound() throws Exception {
    // Arrange
    String aboutId = "1";
    AboutDto aboutDto = AboutDto.builder().id(aboutId).build();
    MockMultipartFile image = mock(MockMultipartFile.class);
    Optional<AboutDto> aboutOptional = Optional.empty();

    when(aboutService.updateAbout(aboutId, aboutDto, image)).thenReturn(aboutOptional);

    // Act
    ResultActions resultActions = mockMvc.perform(
        multipart("/api/about/{id}", aboutId)
            .file(image)
            .param("aboutDto", "{ \"title\": \"Updated Title\", \"description\": \"Updated Description\" }")
    );

    // Assert
    resultActions.andExpect(status().isNotFound());

    verify(aboutService, times(1)).updateAbout(aboutId, aboutDto, image);
  }

//  @Test
  void testDeleteAbout() throws Exception {
    // Arrange
    String aboutId = "1";

    // Act
    ResultActions resultActions = mockMvc.perform(delete("/api/about/{id}", aboutId));

    // Assert
    resultActions.andExpect(status().isOk());

    verify(aboutService, times(1)).deleteAbout(aboutId);
  }
}
