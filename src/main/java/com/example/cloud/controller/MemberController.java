package com.example.cloud.controller;

import com.example.cloud.dto.CreateMemberRequest;
import com.example.cloud.dto.CreateMemberResponse;
import com.example.cloud.dto.GetMemberResponse;
import com.example.cloud.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
