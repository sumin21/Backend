package com.backend.doyouhave.domain.resign.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResignRequestDto {

    @ApiModelProperty(value = "탈퇴 사유")
    @NotBlank
    private String reason;
}
