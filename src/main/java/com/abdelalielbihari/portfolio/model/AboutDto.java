package com.abdelalielbihari.portfolio.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AboutDto implements Serializable {
    private String title;
    private String description;
    private String imageUrl;
}
