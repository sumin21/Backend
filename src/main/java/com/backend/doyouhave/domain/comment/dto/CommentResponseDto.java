package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    @ApiParam(value = "댓글 아이디", required = true, example = "1")
    private final Long commentId;

    @ApiParam(value = "댓글 내용", required = true, example = "댓글 내용 예시")
    private final String content;

    @ApiParam(value = "부모 댓글 아이디", required = true, example = "1(원 댓글일 시 commentId와 같고, 대댓글일 시 다르다)")
    private final Long parentId;

    @ApiParam(value = "삭제되었는지 여부", required = true, example = "true")
    private final boolean isRemoved;

    @ApiParam(value = "비밀댓글인지 여부", required = true, example = "true")
    private final boolean isSecret;

    @ApiParam(value = "댓글 작성자 아이디", required = true, example = "1")
    private final Long writerId;

    @ApiParam(value = "전단지 작성자 아이디", required = true, example = "1")
    private final Long userId;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        // 원 댓글일 경우에는 parentId가 자기 Id
        this.parentId = comment.getParent() == null ? comment.getId() : comment.getParent().getId();
        this.isRemoved = comment.isRemoved();
        this.isSecret = comment.isSecret();
        this.writerId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
    }
}
