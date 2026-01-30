package com.example.cloud.dto;

import lombok.Getter;

@Getter
public class GetMemberResponse {
    private final Long id;
    private final String name;
    private final Long age;
    private final String mbti;

    public GetMemberResponse(Long id, String name, Long age, String mbti) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }
}
