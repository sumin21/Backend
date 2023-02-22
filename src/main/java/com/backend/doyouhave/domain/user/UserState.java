package com.backend.doyouhave.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserState {
    NORMAL("NORMAL"),
    SUSPENDED("SUSPENDED");

    private final String value;
}
