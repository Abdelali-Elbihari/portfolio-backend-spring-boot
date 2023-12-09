package com.abdelalielbihari.portfolio.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.abdelalielbihari.portfolio.config.SecurityConfig;
import com.abdelalielbihari.portfolio.controller.AboutController;
import com.abdelalielbihari.portfolio.security.JwtAuthenticationFilter;
import com.abdelalielbihari.portfolio.service.AboutService;
import com.abdelalielbihari.portfolio.util.JwtTokenUtil;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(SecurityConfig.class)
public class SecurityConfigTest {

  private MockMvc mockMvc;
  @Mock
  private AboutService aboutService;

  @Autowired
  FilterChainProxy filterChainProxy;

  private UserDetailsService userDetailService;

  @BeforeEach
  public void setUp() {
    JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    userDetailService = mock(UserDetailsService.class);
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenUtil, userDetailService);

    when(jwtTokenUtil.extractToken(any())).thenReturn("token");
    when(jwtTokenUtil.validateToken("token")).thenReturn(true);
    when(jwtTokenUtil.getUsernameFromToken("token")).thenReturn("user");
    mockMvc = MockMvcBuilders
        .standaloneSetup(new AboutController(aboutService))
        .apply(springSecurity(filterChainProxy))
        .addFilters(jwtAuthenticationFilter)
        .build();
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = "ADMIN")
  public void whenUserHasAdminRole_thenAccessible() throws Exception {
    when(userDetailService.loadUserByUsername("user"))
        .thenReturn(new User(
            "user",
            "pass",
            List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));

    mockMvc.perform(delete("/api/about/{id}", "id"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = "USER")
  public void whenUserHasUserRole_thenNotAccessible() throws Exception {

    mockMvc.perform(delete("/api/about/{id}", "id"))
        .andExpect(status().isUnauthorized());
  }

}
