package com.backend.doyouhave.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoLoginRequestDto {
    @ApiModelProperty(value = "카카오 서버 토큰")
    @NotBlank
    private String code;
}
