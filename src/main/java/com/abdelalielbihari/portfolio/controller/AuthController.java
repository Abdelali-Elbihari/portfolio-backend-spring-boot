package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.model.AuthRequestDto;
import com.abdelalielbihari.portfolio.model.JwtResponseDTO;
import com.abdelalielbihari.portfolio.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;

  @Operation(summary = "Authenticates a user")
  @PostMapping()
  public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
    if (authentication.isAuthenticated()) {
      return JwtResponseDTO.builder()
          .accessToken(jwtTokenUtil.generateToken(authRequestDto.getUsername())).build();
    } else {
      throw new UsernameNotFoundException("invalid user request..!!");
    }
  }
}
