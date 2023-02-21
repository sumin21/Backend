package com.backend.doyouhave.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRefreshRequestDto {
    @ApiModelProperty(value = "refreshToken")
    @NotBlank
    private String refreshToken;
}
