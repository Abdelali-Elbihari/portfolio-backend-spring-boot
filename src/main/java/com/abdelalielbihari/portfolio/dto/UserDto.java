package com.abdelalielbihari.portfolio.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class UserDto extends BaseDto {
  private String id;
  private String username;
  private String password;
  private String email;
  private Set<String> roles;
}