package com.abdelalielbihari.portfolio.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Data
public class About {

  @Id
  private String id;
  private String title;
  private String description;
  private String imageUrl;
}
