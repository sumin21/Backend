package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private final String content;


    @Builder
    public CommentRequestDto(String content) {
        this.content = content;
    }

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.create(content);

        return comment;
    }
}
