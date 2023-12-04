package com.abdelalielbihari.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class About {

  @Id
  private String id;
  private String title;
  private String description;
  private String imageUrl;
}
