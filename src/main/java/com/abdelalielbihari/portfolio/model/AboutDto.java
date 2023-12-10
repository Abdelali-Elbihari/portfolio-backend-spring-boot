package com.abdelalielbihari.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AboutDto {

  private String id;
  private String title;
  private String description;
  private String imageUrl;
}
