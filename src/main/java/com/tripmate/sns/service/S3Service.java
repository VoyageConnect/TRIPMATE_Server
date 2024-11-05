package com.tripmate.sns.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    public String uploadFile(MultipartFile file) {
        // S3에 파일 업로드 로직 추가
        // 예시:
        // String bucketName = "your-s3-bucket";
        // String fileName = "your-directory/" + file.getOriginalFilename();
        // PutObjectRequest request = new PutObjectRequest(bucketName, fileName, file.getInputStream(), new ObjectMetadata());
        // AmazonS3Client.putObject(request);
        // return AmazonS3Client.getUrl(bucketName, fileName).toString();

        // 임시 URL 반환 (S3에 실제로 업로드하지 않음)
        return "https://example-s3-url.com/photo.jpg";
    }
}
