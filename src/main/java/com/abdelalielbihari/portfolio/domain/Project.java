package com.abdelalielbihari.portfolio.domain;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Document(collection = "projects")
public class Project extends BaseEntity {

  @Id
  private String id;
  private String title;
  private String description;
  private String projectLink;
  private String codeLink;
  private String imgUrl;
  private Set<String> tags;
}
