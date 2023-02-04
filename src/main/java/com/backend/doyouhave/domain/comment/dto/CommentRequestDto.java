package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private final User user;
    private final String content;


    @Builder
    public CommentRequestDto(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public Comment toEntity() {
        Comment comment = new Comment();
        comment.create(user, content);

        return comment;
    }
}
