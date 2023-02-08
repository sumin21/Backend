package com.backend.doyouhave.domain.main;

import lombok.Getter;

@Getter
public record MainInfoDto(
        int allCount,
        int todayCount
) { }
