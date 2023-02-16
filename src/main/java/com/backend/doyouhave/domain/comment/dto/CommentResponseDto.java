package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "CommentResponseDTO")
@ApiModel(description = "CommentResponseDTO")
public class CommentResponseDto {

    @ApiModelProperty(value = "댓글 아이디", required = true, example = "1")
    @Schema(description = "댓글 아이디")
    private final Long commentId;

    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 내용 예시")
    @Schema(description = "댓글 내용")
    private final String content;

    @ApiModelProperty(value = "부모 댓글 아이디", required = true, example = "1(원 댓글일 시 commentId와 같고, 대댓글일 시 다르다)")
    @Schema(description = "부모 댓글 아이디")
    private final Long parentId;

    @ApiModelProperty(value = "삭제되었는지 여부", required = true, example = "true")
    @Schema(description = "삭제 여부", required = true)
    private final boolean isRemoved;

    @ApiModelProperty(value = "비밀댓글인지 여부", required = true, example = "true")
    @Schema(description = "비밀댓글 여부", required = true)
    private final boolean isSecret;

    @ApiModelProperty(value = "댓글 작성자 아이디", required = true, example = "1")
    @Schema(description = "댓글 작성자 아이디", required = true)
    private final Long writerId;

    @ApiModelProperty(value = "전단지 작성자 아이디", required = true, example = "1")
    @Schema(description = "전단지 작성자 아이디", required = true)
    private final Long userId;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        // 원 댓글일 경우에는 parentId가 자기 Id
        this.parentId = comment.getParentId();
        this.isRemoved = comment.isRemoved();
        this.isSecret = comment.isSecret();
        this.writerId = comment.getPost().getId();
        this.userId = comment.getUser().getId();
    }
}
