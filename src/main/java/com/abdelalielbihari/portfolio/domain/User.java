package com.abdelalielbihari.portfolio.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Document(collection = "users")
public class User extends BaseEntity {
  @Id
  private String id;
  private String username;
  @JsonIgnore
  private String password;
  private String email;
  private Set<String> roles;
}
