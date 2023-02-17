package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.*;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.Result;
import com.backend.doyouhave.service.result.SingleResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("api/users")
@RestController
public class PostController {
    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "전단지 등록 API", notes = "전단지를 등록한다. (사진 최대 2개 선택, 태그 최대 3개 입력)")
    public ResponseEntity<SingleResult<PostResponseDto>> savePost(
            @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") Long userId,
            @RequestPart PostRequestDto postRequestDto,
            @Parameter(description = "multipart/form-data 형식의 이미지를 input 최대 2개로 입력받습니다.")
            @RequestPart(required = false) MultipartFile imageFile,
            @RequestPart(required = false) MultipartFile imageFileSecond) throws IOException {

        Post post = postRequestDto.toEntity();
        Long savedPostId = postService.savePost(userId, post, imageFile, imageFileSecond);

        // 등록 후 작성한 페이지로 이동
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(new PostResponseDto(savedPostId)));
    }

    @GetMapping("/posts/{postId}/edit")
    @ApiOperation(value = "전단지 수정 정보 조회 API", notes = "수정할 전단지의 기존 정보를 반환한다.")
    public ResponseEntity<SingleResult<PostUpdateResponseDto>> updatePostInfo(
            @PathVariable Long postId) {
        PostUpdateResponseDto updateResponse = postService.getPostInfoForUpdate(postId);
        return ResponseEntity.ok(responseService.getSingleResult(updateResponse));
    }

    @PostMapping("/posts/{postId}/edit")
    @ApiOperation(value = "전단지 수정 API", notes = "전단지를 수정한다.")
    public ResponseEntity<Result> updatePost(
            @PathVariable Long postId,
            @RequestPart PostUpdateRequestDto postUpdateRequestDto,
            @RequestPart MultipartFile updateImage,
            @RequestPart MultipartFile updateImageSecond) throws IOException {
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
            @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") Long userId,
            @PathVariable Long postId) throws IOException {
        postService.deletePost(userId, postId);
        return ResponseEntity.ok(responseService.getSuccessResult());
    }

    @PatchMapping("/posts/{postId}")
    @ApiOperation(value = "전단지 북마크 API", notes = "전단지를 북마크한다.")
    public ResponseEntity<Result> bookMarkPost(
            @PathVariable Long postId,
            @RequestParam(name = "mark") Boolean mark,
             @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") Long userId) {
        if(userId == null) {
            return ResponseEntity.ok(responseService.getFailureResult());
        }

        if (mark == true) {
            postService.markPost(postId, userId);
        } else {
            postService.markDeletePost(postId, userId);
        }
        return ResponseEntity.ok(responseService.getSuccessResult());
    }

    @GetMapping("/posts/{postId}")
    @ApiOperation(value = "전단지 정보 반환 API", notes = "전단지 정보를 반환한다.")
    public ResponseEntity<SingleResult<PostInfoDto>> getPostInfo(
            @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") Long userId,
            @PathVariable Long postId) {
        PostInfoDto result = postService.getPostInfo(userId, postId);
        return ResponseEntity.ok(responseService.getSingleResult(result));
    }

}
