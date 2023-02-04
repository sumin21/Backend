package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.domain.user.dto.UserProfileResponseDto;
import com.backend.doyouhave.exception.NotFoundException;
import com.backend.doyouhave.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;
    public LoginResponseDto signup(Role role, String code) {

        if (role.equals(Role.KAKAO)) {
            String accessToken = authService.getKakaoAccessTokenByCode(code);
            Optional<User> kakaoUser = authService.saveUserInfoByKakaoToken(accessToken);
            Optional<User> existUser = userRepository.findByRoleAndSocialId(Role.KAKAO, kakaoUser.get().getSocialId());
            if(!existUser.isEmpty()){
                return login(existUser.get());
            }
            userRepository.save(kakaoUser.get());
            return login(kakaoUser.get());

        } else if (role.equals(Role.NAVER)) {
            // 미구현
            return null;
        }

        return null;
    }

    public LoginResponseDto login(User user) {
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        authService.updateRefreshToken(user.getId(), refreshToken);

        return LoginResponseDto.from(
                jwtTokenProvider.createAccessToken(user.getId()),
                refreshToken);
    }

    public UserProfileResponseDto findUsersProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException());
        return UserProfileResponseDto.from(user);
    }
}
