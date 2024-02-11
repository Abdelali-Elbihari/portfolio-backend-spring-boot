package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.domain.User;
import com.abdelalielbihari.portfolio.dto.UserDto;
import com.abdelalielbihari.portfolio.service.UserService;
import com.abdelalielbihari.portfolio.util.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final UserMapper userMapper;

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Create a user",
      security = { @SecurityRequirement(name = "JWT Bearer Key")})
  @PostMapping()
  public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
    User user = userService.createUser(userDto);
    return new ResponseEntity<>(user.getId(), HttpStatus.CREATED);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Get a user by name",
      security = { @SecurityRequirement(name = "JWT Bearer Key")})
  @GetMapping("/{username}")
  public ResponseEntity<UserDto> getUserByName(@PathVariable String username) {
    return userService.getUserByName(username)
        .map(userMapper::toUserDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
