package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.notification.dto.NotificationResponseDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostRequestDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.repository.user.UserRepository;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import lombok.RequiredArgsConstructor;
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
    /* 전단지 작성 API */
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> savePost(
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

        PostResponseDto response = new PostResponseDto(savedPostId, "200", "이미지가 업로드 되었습니다.");
        return ResponseEntity.created(uri).body(response);
    }

    /* 최근 알림 API */
    @GetMapping("/mypage/notifications")
    public ResponseEntity<List<NotificationResponseDto>> getNotification(@PathVariable Long userId) {

        // 댓글 생성시 글 작성자의 알림에 추가하는 로직이 필요
        List<NotificationResponseDto> notificationResponseDtos = userService.getNotifications(userId);
        return ResponseEntity.status(200).body(notificationResponseDtos);
    }
}
