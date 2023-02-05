package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.notification.Notification;
import com.backend.doyouhave.domain.notification.dto.NotificationResponseDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.domain.user.dto.UserProfileResponseDto;
import com.backend.doyouhave.exception.NotFoundException;
import com.backend.doyouhave.jwt.JwtTokenProvider;
import com.backend.doyouhave.repository.notification.NotificationRepository;
import com.backend.doyouhave.repository.post.PostRepository;
import com.backend.doyouhave.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
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


    // 최근 알림 중 최대 8개만 뽑아서 반환
    public List<NotificationResponseDto> getNotifications(Long userId) {
        List<NotificationResponseDto> notificationResponseDtos = notificationRepository
                .findTop8ByUserIdOrderByNotifiedDateDesc(userId)
                .stream()
                .map(NotificationResponseDto::new)
                .collect(Collectors.toList());
        if(notificationResponseDtos.isEmpty()) {
            return null;
        } else {
            return notificationResponseDtos;
        }
    }
}
