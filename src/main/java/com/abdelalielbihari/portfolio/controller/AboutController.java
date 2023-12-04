package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.model.AboutDto;
import com.abdelalielbihari.portfolio.service.AboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/about")
public class AboutController {

  private final AboutService aboutService;

  @GetMapping("/{id}")
  public ResponseEntity<AboutDto> getOneAbout(@PathVariable String id) {
    Optional<AboutDto> about = aboutService.getOneAbout(id);
    return about
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public List<AboutDto> getAllAbouts() {
    return aboutService.getAllAbouts();
  }

  @PostMapping(consumes = {"multipart/form-data"})
  public ResponseEntity<AboutDto> addAbout(
      @ModelAttribute("aboutDto") AboutDto aboutDto,
      @RequestParam("image") MultipartFile image)
      throws IOException {

    AboutDto savedAbout = aboutService.addAbout(aboutDto, image);

    return new ResponseEntity<>(savedAbout, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AboutDto> updateAbout(
      @PathVariable("id") String id,
      @ModelAttribute("aboutDto") AboutDto aboutDto,
      @RequestParam MultipartFile image) throws IOException {
    Optional<AboutDto> about = aboutService.updateAbout(id, aboutDto, image);
    return about
        .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteAbout(@PathVariable String id) {
    aboutService.deleteAbout(id);
    return new ResponseEntity<>("About deleted successfully", HttpStatus.OK);
  }
}
