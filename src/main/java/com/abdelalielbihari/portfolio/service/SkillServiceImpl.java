package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.Skill;
import com.abdelalielbihari.portfolio.repository.SkillRepository;
import com.abdelalielbihari.portfolio.util.UrlCache;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class SkillServiceImpl implements SkillService {

  private final SkillRepository skillRepository;
  private final ImageService imageService;
  private final UrlCache urlCache;

  @Override
  public Optional<Skill> getSkill(String id) {
    return skillRepository.findById(id);
  }

  @Override
  public List<Skill> getAllSkills() {
    return skillRepository.findAll();
  }

  @Override
  public Skill addSkill(MultipartFile icon, Skill skill) throws IOException {
    return handleSaveWitImage(icon, skill);
  }

  @Override
  public Optional<Skill> updateSkill(String id, Skill skill, @Valid MultipartFile icon) throws IOException {

    Optional<Skill> existingSkill = skillRepository.findById(id);

    if (existingSkill.isPresent()) {
      Skill updatedSkill = existingSkill.get();
      updatedSkill.setName(skill.getName());
      updatedSkill.setIcon(skill.getIcon());
      updatedSkill.setBgColor(skill.getBgColor());
      skillRepository.save(updatedSkill);
      return Optional.of(handleSaveWitImage(icon, updatedSkill));
    }
    return Optional.empty();
  }

  private Skill handleSaveWitImage(MultipartFile icon, Skill skill) throws IOException {
    // todo replace image instead
    String imageUrl = imageService.uploadImage(icon);
    skill.setIcon(imageUrl);

    Skill newSkill = skillRepository.save(skill);

    newSkill.setIcon(urlCache.getOrGeneratePresignedUrl(newSkill.getIcon()));
    return newSkill;
  }

  @Override
  public void deleteSkill(String id) {
    Optional<Skill> existingSkill = skillRepository.findById(id);
    existingSkill.ifPresent(skillRepository::delete);
  }
}
