package com.backend.doyouhave.domain.user.dto;

import com.backend.doyouhave.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfileResponseDto {
    @NotBlank
    private Long userId;

    @NotBlank
    private String email;

    @NotBlank
    private String img;

    @NotBlank
    private String nickname;

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
