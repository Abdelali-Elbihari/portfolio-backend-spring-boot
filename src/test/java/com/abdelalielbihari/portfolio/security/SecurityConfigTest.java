package com.abdelalielbihari.portfolio.security;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abdelalielbihari.portfolio.domain.User;
import com.abdelalielbihari.portfolio.dto.AuthRequestDto;
import com.abdelalielbihari.portfolio.dto.UserDto;
import com.abdelalielbihari.portfolio.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class SecurityConfigTest {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Autowired
  FilterChainProxy filterChainProxy;

  @MockBean
  private UserService userService;

  @BeforeEach
  public void setUp() {
    when(userService.getUserByName("id")).thenReturn(Optional.of(User.builder().id("id").build()));
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void whenAuthenticatedUserAccessesUserEndpoint_thenAccessible() throws Exception {
    mockMvc.perform(get("/user/{id}", "id"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void whenUnauthenticatedUserAccessesUserEndpoint_thenForbidden() throws Exception {
    mockMvc.perform(get("/user/{id}", "id"))
        .andExpect(status().isForbidden());
  }
}