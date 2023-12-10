package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.model.UserDto;
import com.abdelalielbihari.portfolio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a user",
      security = { @SecurityRequirement(name = "JWT Bearer Key")})
  @PostMapping()
  public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
    userService.createUser(userDto);
    return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
  }
}
