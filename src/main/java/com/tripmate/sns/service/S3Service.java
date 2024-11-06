package com.tripmate.sns.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tripmate.sns.domain.Sns;
import com.tripmate.sns.dto.SnsRequestDTO;
import com.tripmate.sns.repository.SnsRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.DeclareTypeErrorOrWarning;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 s3;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        s3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata));
        return s3.getUrl(bucketName, fileName).toString();
    }
}

