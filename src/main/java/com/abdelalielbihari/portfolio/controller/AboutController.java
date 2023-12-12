package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.domain.About;
import com.abdelalielbihari.portfolio.dto.AboutDto;
import com.abdelalielbihari.portfolio.service.AboutService;
import com.abdelalielbihari.portfolio.util.AboutMapper;
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
@RequestMapping("/api/about")
public class AboutController {

  private final AboutService aboutService;
  private final AboutMapper aboutMapper;

  @Operation(summary = "Get details of an about entry by ID",
      security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @GetMapping("/{id}")
  public ResponseEntity<AboutDto> getOneAbout(@PathVariable String id) {
    Optional<About> about = aboutService.getAbout(id);
    return about
        .map(value -> new ResponseEntity<>(aboutMapper.toAboutDto(value), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @Operation(summary = "Retrieve all about entries",
      security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @GetMapping
  public List<AboutDto> getAllAbouts() {
    return aboutMapper.toAboutDtoList(aboutService.getAllAbouts());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a new about entry",
      security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<AboutDto> addAbout(
      @ModelAttribute("aboutDto") @Valid AboutDto aboutDto,
      @RequestParam("image") MultipartFile image)
      throws IOException {

    About savedAbout = aboutService.addAbout(aboutMapper.toAbout(aboutDto), image);

    return new ResponseEntity<>(aboutMapper.toAboutDto(savedAbout), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Update an existing about entry",
      security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @PutMapping(
      value = "/{id}",
      consumes = {"multipart/form-data"})
  public ResponseEntity<AboutDto> updateAbout(
      @PathVariable("id") String id,
      @ModelAttribute("aboutDto") @Valid AboutDto aboutDto,
      @RequestParam("image") @Valid MultipartFile image)
      throws IOException {
    Optional<About> about = aboutService.updateAbout(id, aboutMapper.toAbout(aboutDto), image);
    return about
        .map(value -> new ResponseEntity<>(aboutMapper.toAboutDto(value), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Delete an about entry by ID",
      security = {@SecurityRequirement(name = "JWT Bearer Key")})
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAbout(@PathVariable String id) {
    aboutService.deleteAbout(id);
    return new ResponseEntity<>("About deleted successfully", HttpStatus.OK);
  }
}
