package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final String content;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
    }
}
