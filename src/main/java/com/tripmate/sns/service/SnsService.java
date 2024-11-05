package com.tripmate.sns.service;

import com.tripmate.sns.domain.Sns;
import com.tripmate.sns.dto.SnsRequestDTO;
import com.tripmate.sns.dto.SnsResponseDTO;
import com.tripmate.sns.repository.SnsRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SnsService {

    private final SnsRepository snsRepository;
    private final S3Service s3Service;

    public SnsService(SnsRepository snsRepository, S3Service s3Service) {
        this.snsRepository = snsRepository;
        this.s3Service = s3Service;
    }

    // 게시글 생성
    public SnsResponseDTO createPost(SnsRequestDTO snsRequestDTO) {
        // S3에 사진 업로드 후 URL 반환 (여기서 S3 업로드 부분은 주석 처리)
        MultipartFile photo = snsRequestDTO.getPhoto();
        String photoUrl = s3Service.uploadFile(photo);

        Sns sns = new Sns();
        sns.setLocation(snsRequestDTO.getLocation());
        sns.setSubLocation(snsRequestDTO.getSubLocation());
        sns.setPhotoUrl(photoUrl);

        sns = snsRepository.save(sns);
        return SnsResponseDTO.fromEntity(sns);
    }

    // 위치별 게시글 조회
    public List<SnsResponseDTO> getPostsByLocation(String location) {
        List<Sns> posts = snsRepository.findAllByLocation(location);
        return posts.stream()
                .map(SnsResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
