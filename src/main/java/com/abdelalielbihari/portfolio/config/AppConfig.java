package com.abdelalielbihari.portfolio.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
  }

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement().addList("Auth"))
        .components(new Components().addSecuritySchemes("JWT Bearer Key", createAPIKeyScheme()))
        .info(new Info()
            .title("Portfolio Backend API")
            .description(
                "Backend API for my Portfolio accessible at [www.abdelalielbihari.com](www.abdelalielbihari.com). "
                    + "The API is built using Spring Boot, providing functionalities to manage and retrieve portfolio-related data.\n")
            .version("1.0")
            .contact(new Contact()
                .name("Abdelali Elbihari")
                .email("bihariel@gmail.com")
                .url("www.abdelalielbihari.com"))
            .license(new License().name("MIT License")));
  }
}