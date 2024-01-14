package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.domain.Skill;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface SkillService {

  Optional<Skill> getSkill(String id);

  List<Skill> getAllSkills();

  Skill addSkill(MultipartFile icon, Skill skill) throws IOException;

  Optional<Skill> updateSkill(String id, Skill skill, MultipartFile icon) throws IOException;

  void deleteSkill(String id);
}
