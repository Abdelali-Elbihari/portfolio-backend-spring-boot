package com.abdelalielbihari.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@Document(collection = "users")
public class User {

  @Id
  private String id;
  private String username;
  @JsonIgnore
  private String password;
  private String email;
  private Set<String> roles;
}
