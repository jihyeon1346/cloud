package com.example.cloud.service;

import com.example.cloud.dto.CreateMemberRequest;
import com.example.cloud.dto.CreateMemberResponse;
import com.example.cloud.dto.GetMemberResponse;
import com.example.cloud.entity.Member;
import com.example.cloud.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public CreateMemberResponse save(CreateMemberRequest request) {
    Member member = new Member(request.getName(), request.getAge(),request.getMbti());
    memberRepository.save(member);
    return new CreateMemberResponse(member.getId(),  member.getName(), member.getAge(), member.getMbti());
    }

    public GetMemberResponse findOne(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalStateException("없는 멤버입니다.")
        );
        return new GetMemberResponse(member.getId(), member.getName(), member.getAge(), member.getMbti());
    }
}
