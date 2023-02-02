package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.post.dto.PostRequestDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("api/user/{userId}")
@RestController
public class PostController {
    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> savePost(
            @PathVariable Long userId,
//            @RequestHeader(value = "authToken") String authToken,
            @RequestPart("savePost") PostRequestDto postRequestDto,
            @RequestPart MultipartFile postImage) throws IOException {

        Post post = postRequestDto.toEntity();

        Post savedPost = postService.savePost(userId, post, postImage);

        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/{id}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(new PostResponseDto(savedPost.getId()));
    }
}
