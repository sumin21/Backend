package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginRequestDto;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/kakao-login")
    public LoginResponseDto loginUser(@RequestBody @Valid LoginRequestDto request) {
        System.out.println("code = " + request.getCode());
        return userService.signup(Role.KAKAO, request.getCode());

    }

    @GetMapping("/profile")
    public Boolean findUsersProfile(@AuthenticationPrincipal Long userId) {
        System.out.println(userId);

        return true;

    }
}
