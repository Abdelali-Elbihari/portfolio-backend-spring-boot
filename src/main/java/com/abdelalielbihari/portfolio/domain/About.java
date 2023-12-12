package com.abdelalielbihari.portfolio.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "abouts")
@Data
@Builder
public class About extends BaseEntity {

  @Id
  private String id;
  private String title;
  private String description;
  private String imageUrl;
}
