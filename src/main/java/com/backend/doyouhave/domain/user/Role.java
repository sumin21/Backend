package com.backend.doyouhave.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    ADMIN("ADMIN");

    private final String value;
}