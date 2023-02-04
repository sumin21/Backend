package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.dto.LoginRequestDto;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.domain.user.dto.UserProfileResponseDto;
import com.backend.doyouhave.service.UserService;
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
    public LoginResponseDto loginUser(@RequestBody @Valid LoginRequestDto request) {
        return userService.signup(Role.KAKAO, request.getCode());

    }

    @GetMapping("/profile")
    public UserProfileResponseDto findUsersProfile(@AuthenticationPrincipal Long userId) {
        return userService.findUsersProfile(userId);

    }
}
