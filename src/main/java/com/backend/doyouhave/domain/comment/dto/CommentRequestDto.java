package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private User user;
    private String content;
    private Post post;
    private Comment parent;


    @Builder
    public CommentRequestDto(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }

    @Builder
    public CommentRequestDto(User user, Post post, String content, Comment parent) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.parent = parent;
    }

    public Comment toEntityParent() {
        Comment comment = new Comment();
        comment.createParent(user, post, content);

        return comment;
    }

    public Comment toEntityChild() {
        Comment comment = new Comment();
        comment.createChild(user, post, content, parent);

        return comment;
    }
}
