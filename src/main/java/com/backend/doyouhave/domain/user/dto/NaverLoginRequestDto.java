package com.backend.doyouhave.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverLoginRequestDto {
    @ApiModelProperty(value = "네이버 서버 토큰")
    @NotBlank
    private String code;

    @ApiModelProperty(value = "네이버 로그인 state")
    @NotBlank
    private String state;
}
