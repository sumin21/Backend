package com.backend.doyouhave.controller;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.comment.dto.CommentRequestDto;
import com.backend.doyouhave.domain.comment.dto.CommentResponseDto;
import com.backend.doyouhave.domain.comment.dto.MyInfoCommentResponseDto;
import com.backend.doyouhave.domain.post.dto.PostResponseDto;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.service.CommentService;
import com.backend.doyouhave.service.PostService;
import com.backend.doyouhave.service.UserService;
import com.backend.doyouhave.service.result.MultipleResult;
import com.backend.doyouhave.service.result.ResponseService;
import com.backend.doyouhave.service.result.Result;
import com.backend.doyouhave.service.result.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users/{userId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    private final ResponseService responseService;

    /* 댓글 생성 API */
    @PostMapping("/posts/{postId}")
    @ApiOperation(value = "댓글 생성", response = CommentResponseDto.class)
    public ResponseEntity<SingleResult<PostResponseDto>> saveComment(@PathVariable("postId") Long postId,
                                                    @RequestBody CommentRequestDto commentRequestDto) {
        if (commentRequestDto.getParent() == null) {
            commentService.saveParent(commentRequestDto);
        } else {
            commentService.saveChild(commentRequestDto);
        }

        return ResponseEntity.ok(responseService.getSingleResult(new PostResponseDto(postId)));
    }

    /* 댓글 수정 API */
    @PostMapping("/posts/{postId}/{commentId}")
    @ApiOperation(value = "댓글 수정", response = CommentResponseDto.class)
    public ResponseEntity<SingleResult<PostResponseDto>> updateComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                                                      @RequestBody CommentRequestDto commentRequestDto) {
        commentService.update(commentId, commentRequestDto);

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/posts/{postId}").buildAndExpand(postId).toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(new PostResponseDto(postId)));
    }

    /* 댓글 삭제 API */
    @DeleteMapping("/posts/{postId}/{commentId}")
    @ApiOperation(value = "댓글 삭제", response = CommentResponseDto.class)
    public ResponseEntity<SingleResult<PostResponseDto>> deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {

        commentService.delete(commentId);

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/posts/{postId}").buildAndExpand(postId).toUri();

        return ResponseEntity.created(uri).body(responseService.getSingleResult(new PostResponseDto(postId)));
    }

    /* 내가 쓴 댓글 API */
    @GetMapping("/myInfo/comment")
    @ApiOperation(value = "내가 쓴 댓글", notes = "내가 쓴 댓글 목록 출력", response = MyInfoCommentResponseDto.class)
    public ResponseEntity<?> myComments(@PathVariable("userId") Long userId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(commentService.findByUser(userId, pageable));
    }
}
