package com.abdelalielbihari.portfolio.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class SkillDto extends BaseDto {

  private String id;
  private String name;
  private String bgColor;
  private String icon;
}
