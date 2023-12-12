package com.abdelalielbihari.portfolio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.abdelalielbihari.portfolio.domain.Skill;
import com.abdelalielbihari.portfolio.repository.SkillRepository;
import com.abdelalielbihari.portfolio.util.UrlCache;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@RequiredArgsConstructor
class SkillServiceTest {

  @Autowired
  private SkillService skillService;

  @MockBean
  private SkillRepository skillRepository;

  @MockBean
  private ImageService imageService;

  @MockBean
  private UrlCache urlCache;


  @Test
  void testGetOneAbout() {
    String id = UUID.randomUUID().toString();
    Skill mockedSkill = Skill.builder().id(id).build();

    when(skillRepository.findById(id)).thenReturn(Optional.ofNullable(mockedSkill));
    Optional<Skill> skill = skillService.getSkill(id);

    assertTrue(skill.isPresent());
  }

  @Test
  void testGetAllAbouts() {

    when(skillRepository.findAll()).thenReturn(List.of(Skill.builder().build()));
    List<Skill> skills = skillService.getAllSkills();

    assertFalse(skills.isEmpty());

  }

  @Test
  void testAddAbout() throws IOException {
    Skill skill = Skill.builder().id(UUID.randomUUID().toString()).build();
    MultipartFile icon = mock(MultipartFile.class);
    String iconUrl = "uploadedIconUrl";
    when(imageService.uploadImage(icon)).thenReturn(iconUrl);
    when(urlCache.getOrGeneratePresignedUrl(iconUrl)).thenReturn("presignedUrl");
    when(skillRepository.save(skill)).thenReturn(skill);
    Skill savedSkill = skillService.addSkill(icon, skill);

    assertNotNull(savedSkill);
    assertNotNull(savedSkill.getId());
  }

  @Test
  void testUpdateAbout() throws IOException {

    Skill existing = Skill.builder().id(UUID.randomUUID().toString()).build();
    String id = existing.getId();
    Skill expected = Skill.builder().id(id).name("new Name").build();
    MultipartFile icon = mock(MultipartFile.class);
    String iconUrl = "uploadedIconUrl";
    when(imageService.uploadImage(icon)).thenReturn(iconUrl);
    when(urlCache.getOrGeneratePresignedUrl(iconUrl)).thenReturn("presignedUrl");
    when(skillRepository.findById(id)).thenReturn(Optional.of(existing));
    when(skillRepository.save(any(Skill.class))).thenReturn(expected);


    Optional<Skill> updatedSkill = skillService.updateSkill(id, expected, icon);

    assertTrue(updatedSkill.isPresent());
    assertNotNull(updatedSkill.get().getId());
    assertEquals(updatedSkill.get().getName(), expected.getName());

  }

  @Test
  void testDeleteAbout() {
    String id = UUID.randomUUID().toString();
    Skill skill = Skill.builder().id(id).build();

    when(skillRepository.findById(id)).thenReturn(Optional.ofNullable(skill));

    skillService.deleteSkill(id);

    verify(skillRepository, times(1)).findById(id);
    verify(skillRepository, times(1)).delete(any());

  }
}
