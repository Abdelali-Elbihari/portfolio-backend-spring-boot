package com.abdelalielbihari.portfolio.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class ProjectDto extends BaseDto {

  private String id;
  private String title;
  private String description;
  private String projectLink;
  private String codeLink;
  private String imgUrl;
  private Set<String> tags;
}
