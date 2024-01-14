package com.abdelalielbihari.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
class BaseDto {

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
