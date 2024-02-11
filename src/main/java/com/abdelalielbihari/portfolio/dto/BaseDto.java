package com.abdelalielbihari.portfolio.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
class BaseDto implements Serializable {

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
