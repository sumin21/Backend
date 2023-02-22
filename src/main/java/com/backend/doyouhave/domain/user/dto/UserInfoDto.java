package com.backend.doyouhave.domain.user.dto;

import com.backend.doyouhave.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class UserInfoDto {

    @ApiModelProperty(value = "이메일")
    @NotBlank
    private String email;

    @ApiModelProperty(value = "닉네임 (소셜 계정 이름)")
    @NotBlank
    private String nickname;

    @ApiModelProperty(value = "가입 일자")
    @NotBlank
    private String signupDate;

    @ApiModelProperty(value = "최근 접속 일자")
    @NotBlank
    private String recentDate;

    @ApiModelProperty(value = "상태(정상, 이용 제한)")
    @NotBlank
    private String userState;

    public UserInfoDto (User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.signupDate = String.valueOf(user.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.recentDate = String.valueOf(user.getRecentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.userState = user.getUserState().getValue();
    }
}
