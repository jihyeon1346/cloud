package com.example.cloud.member.controller;

import com.example.cloud.member.dto.FileUploadResponse;
import com.example.cloud.member.dto.CreateMemberRequest;
import com.example.cloud.member.dto.CreateMemberResponse;
import com.example.cloud.member.dto.FileDownloadUrlResponse;
import com.example.cloud.member.dto.GetMemberResponse;
import com.example.cloud.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> create(@RequestBody CreateMemberRequest request){
        log.info("[API - LOG] 멤버생성 요청");
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.save(request));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<GetMemberResponse> getOne(@PathVariable Long memberId){
        log.info("[API - LOG] 멤버조회 요청");
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findOne(memberId));
    }

    @PostMapping("/{memberId}/profile-image")
    public ResponseEntity<FileUploadResponse> uploadProfileImage(
            @PathVariable Long memberId,
            @RequestParam("file") MultipartFile file) {
        log.info("[API - LOG] 프로필 이미지 업로드 요청 - memberId: {}", memberId);
        String key = memberService.uploadProfileImage(memberId, file);
        return ResponseEntity.ok(new FileUploadResponse(key));
    }

    @GetMapping("/{memberId}/profile-image")
    public ResponseEntity<FileDownloadUrlResponse> getProfileImageUrl(@PathVariable Long memberId) {
        log.info("[API - LOG] 프로필 이미지 URL 조회 요청 - memberId: {}", memberId);
        String  presignedUrl  = memberService.getProfileImagePresignedUrl(memberId);
        return ResponseEntity.ok(new FileDownloadUrlResponse(presignedUrl));
    }
}
