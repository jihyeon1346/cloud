package com.example.cloud.member.service;

import com.example.cloud.exception.MemberNotFoundException;
import com.example.cloud.exception.ProfileNotFoundException;
import com.example.cloud.exception.UploadFailedException;
import com.example.cloud.member.dto.CreateMemberRequest;
import com.example.cloud.member.dto.CreateMemberResponse;
import com.example.cloud.member.dto.GetMemberResponse;
import com.example.cloud.member.entity.Member;
import com.example.cloud.member.repository.MemberRepository;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public CreateMemberResponse save(CreateMemberRequest request) {
    Member member = new Member(request.getName(), request.getAge(),request.getMbti());
    memberRepository.save(member);
    return new CreateMemberResponse(member.getId(),  member.getName(), member.getAge(), member.getMbti());
    }

    @Transactional(readOnly = true)
    public GetMemberResponse findOne(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("없는 멤버입니다.")
        );

        return new GetMemberResponse(member.getId(), member.getName(), member.getAge(), member.getMbti());
    }

    @Transactional
    public String uploadProfileImage(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("없는 멤버입니다."));

        // S3에 업로드
        String key = upload(file);

        // 멤버 엔티티에 키 저장
        member.updateProfileImage(key);

        return key;
    }

    @Transactional(readOnly = true)
    public String getProfileImagePresignedUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("없는 멤버입니다."));

        if (member.getProfileImageKey() == null) {
            throw new ProfileNotFoundException("프로필 이미지가 없습니다.");
        }

        return getPresignedUrl(member.getProfileImageKey()).toString();
    }

    public String upload(MultipartFile file) {
        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            s3Template.upload(bucket, key, file.getInputStream());
            return key;
        } catch (IOException e) {
            throw new UploadFailedException("파일 업로드 실패");
        }
    }
    // Presigned URL 생성
    public URL getPresignedUrl(String key) {
        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
    }

}
