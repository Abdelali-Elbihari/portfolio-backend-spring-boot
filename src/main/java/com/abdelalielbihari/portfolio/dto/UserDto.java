package com.abdelalielbihari.portfolio.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

  private String username;
  private String password;
  private String email;
  private Set<String> roles;
}