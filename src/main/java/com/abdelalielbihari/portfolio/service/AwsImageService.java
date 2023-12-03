package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.config.AwsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Client s3Client;

    public String uploadImage(MultipartFile imageFile) {
        try {
            String fileName = generateFileName(imageFile.getOriginalFilename());
            PutObjectResponse response = uploadToS3(fileName, imageFile);
            return response.getValueForField("Location", String.class)
                    .orElse(getUploadedFileUrl(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    private PutObjectResponse uploadToS3(String fileName, MultipartFile imageFile) throws IOException {
        byte[] bytes = imageFile.getBytes();
        return s3Client.putObject(PutObjectRequest.builder()
                .bucket(AwsConfig.BUCKET_NAME)
                .key(fileName)
                .build(), RequestBody.fromBytes(bytes));
    }

    private String getUploadedFileUrl(String fileName) {
        return "https://" + AwsConfig.BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
    }
}
