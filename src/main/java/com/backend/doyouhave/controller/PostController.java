package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.notification.dto.NotificationResponseDto;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostRequestDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.domain.post.dto.PostUpdateRequestDto;
import com.backend.doyouhave.domain.post.dto.PostUpdateResponseDto;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.exception.ExceptionCode;
import com.backend.doyouhave.exception.ExceptionResponse;
import com.backend.doyouhave.exception.NotFoundException;
import com.backend.doyouhave.jwt.UserPrincipal;
import com.backend.doyouhave.repository.user.UserRepository;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.Result;
import com.backend.doyouhave.service.result.SingleResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final ResponseService responseService;

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "전단지 등록 API", notes = "전단지를 등록한다. (사진 최대 2개 선택, 태그 최대 3개 입력)")
    public ResponseEntity<SingleResult> savePost(
//            @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : userId") Long userId,
            @RequestPart(name="dto") PostRequestDto postRequestDto,
            @Parameter(description = "multipart/form-data 형식의 이미지를 input 최대 2개로 입력받습니다.")
            @RequestPart(name="img", required = false) MultipartFile imageFile,
            @RequestPart(name="imgSecond", required = false) MultipartFile imageFileSecond) throws IOException {

        Post post = postRequestDto.toEntity();
        Long savedPostId = postService.savePost(post, imageFile, imageFileSecond);

        // 등록 후 작성한 페이지로 이동
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(new PostResponseDto(savedPostId)));
    }

    @GetMapping("/posts/{postId}/edit")
    @ApiOperation(value = "전단지 수정 정보 조회 API", notes = "수정할 전단지의 기존 정보를 반환한다.")
    public ResponseEntity<?> updatePostInfo(
            @PathVariable Long postId) {
        PostUpdateResponseDto updateResponse = postService.getPostInfo(postId);
        if(updateResponse == null) {
            return ResponseEntity.ok(ExceptionResponse.of(ExceptionCode.NOT_FOUND, "전단지가 존재하지 않습니다."));
        }
        return ResponseEntity.ok(responseService.getSingleResult(updateResponse));
    }

    @PostMapping("/posts/{postId}/edit")
    @ApiOperation(value = "전단지 수정 API", notes = "전단지를 수정한다.")
    public ResponseEntity<Result> updatePost(
            @PathVariable Long postId,
            @RequestPart(value="updatePost") PostUpdateRequestDto postUpdateRequestDto,
            @RequestPart(value="img") MultipartFile updateImage,
            @RequestPart(value="imgSecond") MultipartFile updateImageSecond) throws IOException {
        // 수정된 정보들을 받아 수정 처리
        Post updatedPost = postService.updatePost(postId, postUpdateRequestDto, updateImage, updateImageSecond);

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/api/users/posts/{postId}")
                .buildAndExpand(postId)
                .toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(updatedPost.getId()));
    }

    @DeleteMapping("/posts/{postId}")
    @ApiOperation(value = "전단지 삭제 API", notes = "전단지를 삭제한다.")
    public ResponseEntity<Result> deletePost(
            @PathVariable Long postId) throws IOException {
        postService.deletePost(postId);
        return ResponseEntity.ok(responseService.getSuccessResult());
    }

    @PatchMapping("/posts/{postId}")
    @ApiOperation(value = "전단지 북마크 API", notes = "전단지를 북마크한다.")
    public ResponseEntity<?> bookMarkPost(
            @PathVariable Long postId,
            @RequestParam(name = "mark") boolean mark,
             @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : userId") Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(ExceptionResponse.of(ExceptionCode.FAIL_AUTHENTICATION, "회원만 가능합니다."));
        }
        postService.markPost(userId, postId, mark);
        return ResponseEntity.ok(responseService.getSuccessResult());
    }

}
