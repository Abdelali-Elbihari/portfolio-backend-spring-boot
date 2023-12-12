package com.abdelalielbihari.portfolio.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Document(collection = "skills")
public class Skill extends BaseEntity {
  @Id
  private String id;
  private String name;
  private String bgColor;
  private String icon;
}
