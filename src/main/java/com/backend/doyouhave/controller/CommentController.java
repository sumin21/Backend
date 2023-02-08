package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.service.CommentService;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.Result;
import com.backend.doyouhave.service.result.SingleResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final ResponseService responseService;

    /* 댓글 생성 API */
    @PostMapping("/posts/{postId}")
    public ResponseEntity<SingleResult> saveComment(@PathVariable("postId") Long postId,
                                                           @RequestBody CommentRequestDto commentRequestDto) {
        if (commentRequestDto.getParent() == null) {
            commentService.saveParent(commentRequestDto);
        } else {
            commentService.saveChild(commentRequestDto);
        }

        return ResponseEntity.ok(responseService.getSingleResult(new PostResponseDto(postId)));
    }

    /* 댓글 삭제 API */
    @DeleteMapping("/posts/{postId}/{commentId}")
    public ResponseEntity<SingleResult> deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {

        commentService.delete(commentId);

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/posts/{postId}").buildAndExpand(postId).toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(new PostResponseDto(postId)));
    }
}
