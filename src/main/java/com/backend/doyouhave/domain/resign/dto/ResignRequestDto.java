package com.backend.doyouhave.domain.resign.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResignRequestDto {
    @NotBlank
    private String reason;
}
