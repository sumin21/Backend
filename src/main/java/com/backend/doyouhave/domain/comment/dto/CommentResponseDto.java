package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private final Long commentId;
    private final String content;
    private final Long parentId;
    private final boolean isRemoved;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        // 원 댓글일 경우에는 parentId가 자기 Id
        this.parentId = comment.getParent() == null ? comment.getId() : comment.getParent().getId();
        this.isRemoved = comment.isRemoved();
    }
}
