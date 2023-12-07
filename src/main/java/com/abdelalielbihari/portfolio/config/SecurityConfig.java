
package com.abdelalielbihari.portfolio.config;

import com.abdelalielbihari.portfolio.security.JwtAuthenticationFilter;
import com.abdelalielbihari.portfolio.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtAuthenticationFilter jwtAuthorizationFilter;

  public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationFilter jwtAuthorizationFilter) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthorizationFilter = jwtAuthorizationFilter;
  }

//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.cors(AbstractHttpConfigurer::disable)
//        .csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests((authorize) -> authorize
////            .requestMatchers("/api/login/***").permitAll()
////            .requestMatchers("/api/***").authenticated()
//                .anyRequest().authenticated()
//        )
//        .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .authenticationProvider(authenticationProvider())
//        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//    return http.build();
//  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;

  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

//  @Bean
//  public AuthenticationManager authenticationManager(HttpSecurity http)
//      throws Exception {
//    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    return authenticationManagerBuilder.build();
//  }

}
