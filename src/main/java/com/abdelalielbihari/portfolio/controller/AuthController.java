package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.model.AuthRequestDto;
import com.abdelalielbihari.portfolio.model.JwtResponseDTO;
import com.abdelalielbihari.portfolio.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;

  @Operation(summary = "Authenticates a user and returns a Jwt token")
  @PostMapping()
  public ResponseEntity<JwtResponseDTO> AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
    if (authentication.isAuthenticated()) {
      JwtResponseDTO jwtResponseDTO = JwtResponseDTO.builder()
          .accessToken(jwtTokenProvider.generateToken(authRequestDto.getUsername())).build();
      return new ResponseEntity<>(jwtResponseDTO, HttpStatus.CREATED);
    } else {
      throw new UsernameNotFoundException("invalid user request..!!");
    }
  }
}
