package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.comment.dto.CommentResponseDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.dto.KakaoLoginRequestDto;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.domain.user.dto.NaverLoginRequestDto;
import com.backend.doyouhave.domain.user.dto.UserProfileResponseDto;
import com.backend.doyouhave.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/kakao-login")
    @ApiOperation(value = "카카오 로그인 API", response = UserProfileResponseDto.class)
    public LoginResponseDto kakaoLoginUser(@RequestBody @Valid KakaoLoginRequestDto request) {
        return userService.signup(Role.KAKAO, request.getCode(), null);

    }

    @PostMapping("/naver-login")
    @ApiOperation(value = "네이버 로그인 API", response = UserProfileResponseDto.class)
    public LoginResponseDto naverLoginUser(@RequestBody @Valid NaverLoginRequestDto request) {
        return userService.signup(Role.NAVER, request.getCode(), request.getState());

    }

    @GetMapping("/profile")
    @ApiOperation(value = "사용자 프로필 조회 API", response = UserProfileResponseDto.class)
    public UserProfileResponseDto findUsersProfile(@AuthenticationPrincipal Long userId) {
        return userService.findUsersProfile(userId);

    }
}
