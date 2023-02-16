package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.notification.dto.NotificationResponseDto;
import com.backend.doyouhave.domain.post.dto.PostListResponseDto;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.dto.KakaoLoginRequestDto;
import com.backend.doyouhave.domain.user.dto.LoginResponseDto;
import com.backend.doyouhave.domain.user.dto.NaverLoginRequestDto;
import com.backend.doyouhave.domain.user.dto.UserProfileResponseDto;
import com.backend.doyouhave.exception.ExceptionCode;
import com.backend.doyouhave.exception.ExceptionResponse;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import com.backend.doyouhave.service.result.MultiplePageResult;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private final ResponseService responseService;


    /* 카카오 로그인 API */
    @PostMapping("/kakao-login")
    @ApiOperation(value = "카카오 로그인 API", response = UserProfileResponseDto.class)
    public LoginResponseDto kakaoLoginUser(@RequestBody @Valid KakaoLoginRequestDto request) {
        return userService.signup(Role.KAKAO, request.getCode(), null);

    }

    /* 네이버 로그인 API */
    @PostMapping("/naver-login")
    @ApiOperation(value = "네이버 로그인 API", response = UserProfileResponseDto.class)
    public LoginResponseDto naverLoginUser(@RequestBody @Valid NaverLoginRequestDto request) {
        return userService.signup(Role.NAVER, request.getCode(), request.getState());

    }

    /* 사용자 프로필 반환 API */
    @GetMapping("/profile")
    @ApiOperation(value = "사용자 프로필 조회 API", response = UserProfileResponseDto.class)
    public ResponseEntity<SingleResult<UserProfileResponseDto>> findUsersProfile(@AuthenticationPrincipal Long userId) {
        UserProfileResponseDto result = userService.findUsersProfile(userId);
        return ResponseEntity.ok(responseService.getSingleResult(result));

    }

    /* 사용자가 작성한 포스트 목록 반환 API */
    @GetMapping("/mypage/posts")
    @ApiOperation(value = "사용자가 작성한 포스트 목록 반환 API")
    public ResponseEntity<MultiplePageResult<PostListResponseDto>> usersPostListUp(
            @AuthenticationPrincipal Long userId,
            @PageableDefault(size=10) Pageable pageable) {

        Page<PostListResponseDto> response = postService.findUsersPostByUserId(userId, pageable);
        return ResponseEntity.ok(responseService.getMultiplePageResult(response));
    }

    /* 최근 알림 API */
    @GetMapping("/mypage/notifications")
    @ApiOperation(value = "최근 알림 API", notes = "마이페이지에서 최근 알림을 조회한다. (최대 알림 8개) ")
    public ResponseEntity<MultipleResult<NotificationResponseDto>> getNotification(@AuthenticationPrincipal Long userId) {

        List<NotificationResponseDto> notificationResponseDtos = userService.getNotifications(userId);
        return ResponseEntity.ok(responseService.getMultipleResult(notificationResponseDtos));
    }
    
    /* 내 북마크 목록 API */
    @GetMapping("/mypage/marks")
    @ApiOperation(value = "내 북마크 목록 API", notes = "마이페이지에서 내가 북마크한 게시글들의 기본 정보를 반환한다.")
    public ResponseEntity<MultiplePageResult<PostListResponseDto>> usersLikes(
            @AuthenticationPrincipal Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PostListResponseDto> postListResponseDtos = userService.marksList(userId, pageable);
        return ResponseEntity.ok(responseService.getMultiplePageResult(postListResponseDtos));
    }
}
