package com.backend.doyouhave.service;

import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.notification.Notification;
import com.backend.doyouhave.domain.notification.dto.NotificationResponseDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostListResponseDto;
import com.backend.doyouhave.domain.post.dto.ReportedPostDto;
import com.backend.doyouhave.domain.resign.ResignReason;
import com.backend.doyouhave.domain.resign.dto.ResignRequestDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.domain.user.dto.UserInfoDto;
import com.backend.doyouhave.domain.user.dto.UserProfileResponseDto;
import com.backend.doyouhave.exception.NotFoundException;
import com.backend.doyouhave.jwt.JwtTokenProvider;
import com.backend.doyouhave.repository.notification.NotificationRepository;
import com.backend.doyouhave.repository.post.PostRepository;
import com.backend.doyouhave.repository.resign.ResignReasonRepository;
import com.backend.doyouhave.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;
    private final ResignReasonRepository resignReasonRepository;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponseDto signup(Role role, String code, String state) {
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
            String accessToken = authService.getNaverAccessTokenByCode(code, state);
            Optional<User> naverUser = authService.saveUserInfoByNaverToken(accessToken);
            Optional<User> existUser = userRepository.findByRoleAndSocialId(Role.NAVER, naverUser.get().getSocialId());
            if(!existUser.isEmpty()){
                return login(existUser.get());
            }
            userRepository.save(naverUser.get());
            return login(naverUser.get());
        }

        return null;
    }

    public LoginResponseDto login(User user) {
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getId());
        authService.updateRefreshToken(user.getId(), refreshToken);
        user.setRecentDate(LocalDateTime.now());

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
        return notificationResponseDtos;
    }

    // 회원의 북마크 전단지 목록 반환
    public Page<PostListResponseDto> marksList(Long userId, Pageable pageable) {
        Page<PostListResponseDto> markedPostResponseDtos = postRepository
                .findMarkedPostByUserId(userId, pageable)
                .map(PostListResponseDto::new);
        return markedPostResponseDtos;
    }

    // refresh token 삭제
    public void deleteRefreshToken(Long userId) {
        authService.updateRefreshToken(userId, null);
    }

    // 사용자 삭제
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // 사용자 탈퇴 사유 저장
    public void saveResignReason(String reason) {
        Optional<ResignReason> optionalResignReason = resignReasonRepository.findByReason(reason);
        if(optionalResignReason.isPresent()){
            ResignReason resignReason = optionalResignReason.get();
            resignReason.setCount(resignReason.getCount() + 1);
            resignReasonRepository.save(resignReason);
        } else {
            resignReasonRepository.save(ResignReason.from(reason));
        }
    }

    public Page<UserInfoDto> findUsersInfo(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserInfoDto::new);
    }
}
