package com.abdelalielbihari.portfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "users")
@Builder
@Data
public class User {
  @Id
  private String id;
  private String username;
  @JsonIgnore
  private String password;
  private String email;
  private Set<String> roles;
}
