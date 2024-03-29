package com.abdelalielbihari.portfolio.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AboutDto extends BaseDto {

  private String id;
  private String title;
  private String description;
  private String imgUrl;
}
