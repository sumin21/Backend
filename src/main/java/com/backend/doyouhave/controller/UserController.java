package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostRequestDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginRequestDto;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/kakao-login")
    public LoginResponseDto loginUser(@RequestBody LoginRequestDto request) {
        System.out.println("code = " + request.getCode());
        return userService.signup(Role.KAKAO, request.getCode());

    }
}
