package com.abdelalielbihari.portfolio.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsImageService implements ImageService {

  @Value("${cloud.aws.s3.bucket}")
  public String bucketName;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;


  @Override
  public String uploadImage(MultipartFile imageFile) {
    try {
      String fileName = generateFileName(imageFile.getOriginalFilename());
      PutObjectResponse response = uploadToS3(fileName, imageFile.getBytes());
      return response.getValueForField("Location", String.class)
          .orElse(getUploadedFileUrl(fileName));
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload image", e);
    }
  }

  @Override
  public String getPresignedImageUrl(String imageUrl) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(getKeyFromUrl(imageUrl))
        .build();

    GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(120))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

    return presignedGetObjectRequest.url().toString();
  }

  private String getKeyFromUrl(String imageUrl) {
    return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
  }

  private String generateFileName(String originalFileName) {
    return UUID.randomUUID() + "_" + originalFileName;
  }

  private PutObjectResponse uploadToS3(String fileName, byte[] file) throws IOException {
    return s3Client.putObject(PutObjectRequest.builder()
        .bucket(bucketName)
        .key("images" + "/" + fileName)
        .build(), RequestBody.fromBytes(file));
  }

  private String getUploadedFileUrl(String fileName) {
    return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
  }
}
