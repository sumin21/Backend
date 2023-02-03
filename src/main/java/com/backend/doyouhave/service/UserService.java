package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.repository.post.PostRepository;
import com.backend.doyouhave.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public LoginResponseDto signup(Role role, String code) {

        if (role.equals(Role.KAKAO)) {
            String accessToken = authService.getAccessTokenByCode(code);
            Optional<User> kakaoUser = authService.saveUserInfoByToken(accessToken);
            Optional<User> existUser = userRepository.findByRoleAndSocialId(Role.KAKAO, kakaoUser.get().getSocialId());
            if(!existUser.isEmpty()){
                // exist
                return login(existUser.get());
            }
            // signup
            userRepository.save(kakaoUser.get());
            return login(kakaoUser.get());

        } else if (role.equals(Role.NAVER)) {
            // 미구현
            return null;
        }

        return null;
    }

    public LoginResponseDto login(User user) {
        // JWT 구현 예정

        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        return LoginResponseDto.from(accessToken, refreshToken);
    }
}
