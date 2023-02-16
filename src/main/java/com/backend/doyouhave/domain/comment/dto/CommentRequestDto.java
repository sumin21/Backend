package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "CommentRequestDTO")
@ApiModel(description = "CommentRequestDTO")
public class CommentRequestDto {

    @ApiModelProperty(value = "댓글 내용", required = true)
    @Schema(description = "댓글 내용", required = true)
    private String content;

    @ApiModelProperty(value = "원 댓글 아이디")
    @Schema(description = "원 댓글 아이디")
    private Long parentId;

    @ApiModelProperty(value = "비밀 댓글 여부", required = true)
    @Schema(description = "비밀 댓글 여부", required = true)
    private boolean isSecret;


    @Builder
    public CommentRequestDto(String content, boolean isSecret) {
        this.content = content;
        this.isSecret = isSecret;
    }

    @Builder
    public CommentRequestDto(String content, Long parentId, boolean isSecret) {
        this.content = content;
        this.parentId = parentId;
        this.isSecret = isSecret;
    }

    public Comment toEntity(User user, Post post) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.create(user, post, content, parentId, isSecret);

        return comment;
    }
}
