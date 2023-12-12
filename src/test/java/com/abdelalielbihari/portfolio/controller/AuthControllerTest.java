package com.abdelalielbihari.portfolio.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abdelalielbihari.portfolio.dto.AuthRequestDto;
import com.abdelalielbihari.portfolio.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class AuthControllerTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authenticationManager, jwtTokenProvider)).build();
  }

  @Test
  void testAuthenticateAndGetToken() throws Exception {
    // Arrange
    AuthRequestDto authRequestDto = new AuthRequestDto("username", "password");
    String fakeToken = "fakeToken";

    UsernamePasswordAuthenticationToken mockedAuthenticationToken = new UsernamePasswordAuthenticationToken(
        authRequestDto.getUsername(),
        authRequestDto.getPassword(),
        List.of(new SimpleGrantedAuthority("ADMIN")));

    when(authenticationManager.authenticate(any()))
        .thenReturn(mockedAuthenticationToken);
    when(jwtTokenProvider.generateToken(authRequestDto.getUsername())).thenReturn(fakeToken);

    // Act
    ResultActions resultActions = mockMvc.perform(post("/auth")
        .contentType("application/json")
        .content(asJsonString(authRequestDto)));

    // Assert
    resultActions.andExpect(status().isCreated())
        .andExpect(jsonPath("$.accessToken").value(fakeToken));
  }

  @Test
  void testAuthenticateAndGetTokenInvalidUser() throws Exception {
    // Arrange
    AuthRequestDto authRequestDto = new AuthRequestDto("invalidUser", "invalidPassword");
    UsernamePasswordAuthenticationToken mockedAuthenticationToken = new UsernamePasswordAuthenticationToken(
        "InvalidUser",
        "");

    mockedAuthenticationToken.setAuthenticated(false);

    when(authenticationManager.authenticate(any()))
        .thenReturn(mockedAuthenticationToken);

    try {
      // Act
      mockMvc.perform(post("/auth")
          .contentType("application/json")
          .content(asJsonString(authRequestDto)));
      fail("Fail due to Authentication successful for Invalid User");
    } catch (Exception e) {
      assert (e.getCause().getMessage().equals("invalid user request..!!"));
    }
  }

  private static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
