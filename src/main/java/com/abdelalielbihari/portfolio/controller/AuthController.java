package com.abdelalielbihari.portfolio.controller;

import com.abdelalielbihari.portfolio.dto.AuthRequestDto;
import com.abdelalielbihari.portfolio.dto.JwtResponseDTO;
import com.abdelalielbihari.portfolio.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final JwtTokenUtil jwtTokenUtil;

  @Operation(summary = "Authenticates a user and returns a Jwt token")
  @PostMapping()
  public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(authRequestDto.getUsername(), authRequestDto.getPassword()));
    if (authentication.isAuthenticated()) {
      return JwtResponseDTO.builder().accessToken(jwtTokenUtil.generateToken(authRequestDto.getUsername())).build();
    } else {
      throw new UsernameNotFoundException("invalid user request..!!");
    }
  }
}
