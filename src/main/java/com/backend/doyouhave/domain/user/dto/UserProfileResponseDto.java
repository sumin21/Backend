package com.backend.doyouhave.domain.user.dto;

import com.backend.doyouhave.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileResponseDto {
    @ApiModelProperty(value = "사용자 id")
    @NotBlank
    private Long userId;

    @ApiModelProperty(value = "이메일")
    @NotBlank
    private String email;

    @ApiModelProperty(value = "프로필 사진")
    @NotBlank
    private String img;

    @ApiModelProperty(value = "닉네임 (소셜 계정 이름)")
    @NotBlank
    private String nickname;

    @ApiModelProperty(value = "소셜 로그인 유형 (카카오/네이버)")
    @NotBlank
    private String socialType;

    @Builder
    public UserProfileResponseDto(Long userId, String email, String img, String nickname, String socialType) {
        this.userId = userId;
        this.email = email;
        this.img = img;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    public static UserProfileResponseDto from(User user) {
        return UserProfileResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .img(user.getImg())
                .nickname(user.getNickname())
                .socialType(user.getRole().getValue())
                .build();
    }
}
