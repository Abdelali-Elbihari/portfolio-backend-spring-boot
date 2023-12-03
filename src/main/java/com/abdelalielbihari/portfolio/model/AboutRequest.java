package com.abdelalielbihari.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class AboutRequest {
    AboutDto aboutDto;
    MultipartFile image;
}
