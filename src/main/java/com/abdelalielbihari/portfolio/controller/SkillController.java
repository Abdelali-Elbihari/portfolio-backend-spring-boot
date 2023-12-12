package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.domain.Skill;
import com.abdelalielbihari.portfolio.dto.SkillDto;
import com.abdelalielbihari.portfolio.service.SkillService;
import com.abdelalielbihari.portfolio.util.SkillMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/skill")
public class SkillController {

  private final SkillService skillService;
  private final SkillMapper skillMapper;

  @Operation(summary = "Get details of an skill entry by ID", security = {
      @SecurityRequirement(name = "JWT Bearer Key")})
  @GetMapping("/{id}")
  public ResponseEntity<SkillDto> getOneSkill(@PathVariable String id) {
    Optional<Skill> skill = skillService.getSkill(id);
    return skill.map(value -> new ResponseEntity<>(skillMapper.toSkillDto(value), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Retrieve all skill entries", security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @GetMapping
  public List<SkillDto> getAllSkills() {
    return skillMapper.toSkillDtos(skillService.getAllSkills());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new skill entry", security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<SkillDto> addSkill(@ModelAttribute("skillDto") @Valid SkillDto skillDto,
      @RequestParam("icon") @Valid MultipartFile icon) throws IOException {

    Skill savedSkill = skillService.addSkill(icon, skillMapper.toSkill(skillDto));

    return new ResponseEntity<>(skillMapper.toSkillDto(savedSkill), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update an existing skill entry", security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
  public ResponseEntity<SkillDto> updateSkill(@PathVariable("id") String id,
      @ModelAttribute("skillDto") @Valid SkillDto skillDto, @RequestParam("icon") @Valid MultipartFile icon)
      throws IOException {
    Optional<Skill> skill = skillService.updateSkill(id, skillMapper.toSkill(skillDto), icon);
    return skill.map(value -> new ResponseEntity<>(skillMapper.toSkillDto(value), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete an skill entry by ID", security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteSkill(@PathVariable String id) {
    skillService.deleteSkill(id);
    return new ResponseEntity<>("Skill deleted successfully", HttpStatus.OK);
  }
}
