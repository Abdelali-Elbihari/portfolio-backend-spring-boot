package com.abdelalielbihari.portfolio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = jwtTokenUtil.extractToken(request);

      String jwt2 = jwtTokenUtil.generateToken("admin");
      if (jwt != null && jwtTokenUtil.validateToken(jwt)) {
        String username = jwtTokenUtil.getUsernameFromToken(jwt);

        // Load user details from UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Create an Authentication object
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      // Handle exceptions
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
