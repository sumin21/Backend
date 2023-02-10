package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import io.swagger.annotations.ApiParam;
import lombok.Builder;
import lombok.Getter;

/*
 * 내가 쓴 댓글 조회 시에 사용하는 DTO
 */
@Getter
public class MyInfoCommentResponseDto {

    @ApiParam(value = "댓글 아이디", required = true, example = "1")
    private final Long commentId;

    @ApiParam(value = "댓글 내용", required = true, example = "댓글 내용 예시")
    private final String content;

    @ApiParam(value = "댓글이 작성된 전단지 아이디", required = true, example = "1")
    private final Long postId;

    @ApiParam(value = "댓글이 작성된 전단지 제목", required = true, example = "OOO 구합니다!")
    private final String post_title;

    @Builder
    public MyInfoCommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.post_title = comment.getPost().getTitle();
    }
}
