package com.backend.doyouhave.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    KAKAO("ROLE_KAKAO"),
    NAVER("ROLE_NAVER"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}