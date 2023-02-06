package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.notification.dto.NotificationResponseDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostRequestDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.exception.ExceptionCode;
import com.backend.doyouhave.exception.ExceptionResponse;
import com.backend.doyouhave.repository.user.UserRepository;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.Result;
import com.backend.doyouhave.service.result.SingleResult;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("api/users/{userId}")
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final ResponseService responseService;

    /* 전단지 작성 API */
    @PostMapping("/posts")
    public ResponseEntity<SingleResult> savePost(
            @PathVariable Long userId,
//            @AuthenticationPrincipal User user,
            @RequestPart(value="dto") PostRequestDto postRequestDto,
            @RequestPart(value="img") MultipartFile imageFile,
            @RequestPart(value="imgSecond", required = false) MultipartFile imageFileSecond) throws IOException {

        Post post = postRequestDto.toEntity();
        Long savedPostId = postService.savePost(post, imageFile, imageFileSecond);

        // 등록 후 작성한 페이지로 이동
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(new PostResponseDto(savedPostId)));
    }

    /* 전단지 삭제 API */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Result> deletePost(
            @PathVariable Long userId,
            @PathVariable Long postId) throws IOException {
        postService.deletePost(postId);
        return ResponseEntity.ok(responseService.getSuccessResult());
    }

    /* 최근 알림 API */
    @GetMapping("/mypage/notifications")
    public ResponseEntity<?> getNotification(@PathVariable Long userId) {

        List<NotificationResponseDto> notificationResponseDtos = userService.getNotifications(userId);
        if(notificationResponseDtos == null) {
            return ResponseEntity.ok(ExceptionResponse.of(ExceptionCode.NOT_FOUND, "알림이 존재하지 않습니다."));
        }

        return ResponseEntity.ok(responseService.getMultipleResult(notificationResponseDtos));
    }
}
