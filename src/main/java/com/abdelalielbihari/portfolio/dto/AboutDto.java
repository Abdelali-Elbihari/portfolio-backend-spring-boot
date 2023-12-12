package com.abdelalielbihari.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AboutDto extends BaseDto {

  private String id;
  private String title;
  private String description;
  private String imageUrl;
}
