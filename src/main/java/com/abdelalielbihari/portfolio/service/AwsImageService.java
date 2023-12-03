package com.abdelalielbihari.portfolio.service;

import com.abdelalielbihari.portfolio.config.AwsConfig;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsImageService implements ImageService {
    @Value("${cloud.aws.s3.bucket}")
    public String bucketName;

    private final S3Client s3Client;

    @Override
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

    @Override
    public URL getImageUrl(String imageUrl) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(getKeyFromUrl(imageUrl))
                .build();

        return s3Client.utilities().getUrl(request);
    }

    private String getKeyFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    private PutObjectResponse uploadToS3(String fileName, MultipartFile imageFile) throws IOException {
        byte[] bytes = imageFile.getBytes();
        return s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build(), RequestBody.fromBytes(bytes));
    }

    private String getUploadedFileUrl(String fileName) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }
}
