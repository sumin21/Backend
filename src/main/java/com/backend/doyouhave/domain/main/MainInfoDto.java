package com.backend.doyouhave.domain.main;

import lombok.Getter;

public record MainInfoDto(
        int allCount,
        int todayCount
) { }
