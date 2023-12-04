package com.abdelalielbihari.portfolio.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    String uploadImage(MultipartFile imageFile) throws IOException;
    String getPresignedImageUrl(String imageUrl);

}
