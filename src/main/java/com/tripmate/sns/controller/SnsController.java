package com.tripmate.sns.controller;

import com.tripmate.sns.dto.SnsRequestDTO;
import com.tripmate.sns.dto.SnsResponseDTO;
import com.tripmate.sns.service.SnsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/sns")
public class SnsController {

    private final SnsService snsService;

    public SnsController(SnsService snsService) {
        this.snsService = snsService;
    }

    // 게시글 작성 API
    @Operation(summary = "게시글 작성", description = "사진을 저장")
    @PostMapping("/post")
    public ResponseEntity<SnsResponseDTO> createPost(
            @RequestParam String location,
            @RequestParam String subLocation,
            @RequestParam("photo") MultipartFile photo
    ) {
        SnsRequestDTO requestDTO = new SnsRequestDTO();
        requestDTO.setLocation(location);
        requestDTO.setSubLocation(subLocation);
        requestDTO.setPhoto(photo);

        SnsResponseDTO newPost = snsService.createPost(requestDTO);
        return ResponseEntity.ok(newPost);
    }

    // 게시글 조회 API
    @Operation(summary = "게시글 조회", description = "위치에 해당하는 사진과 태그를 list 형식으로 전달")
    @GetMapping("/posts")
    public ResponseEntity<List<SnsResponseDTO>> getPostsByLocation(@RequestParam String location) {
        List<SnsResponseDTO> posts = snsService.getPostsByLocation(location);
        return ResponseEntity.ok(posts);
    }
}
