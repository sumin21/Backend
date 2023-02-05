package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

/*
 * 내가 쓴 댓글 조회 시에 사용하는 DTO
 */
@Getter
public class MyInfoCommentResponseDto {

    private final Long commentId;
    private final String content;
    private final Long postId;
    private final String post_title;

    @Builder
    public MyInfoCommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.post_title = comment.getPost().getTitle();
    }
}
