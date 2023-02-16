package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/*
 * 내가 쓴 댓글 조회 시에 사용하는 DTO
 */
@Getter
@Schema(description = "내가 쓴 댓글 응답 DTO")
@ApiModel(description = "내가 쓴 댓글 응답 DTO")
public class MyInfoCommentResponseDto {

    @ApiParam(value = "댓글 아이디", required = true, example = "1")
    @Schema(description = "댓글 아이디")
    private final Long commentId;

    @ApiParam(value = "댓글 내용", required = true, example = "댓글 내용 예시")
    @Schema(description = "댓글 내용")
    private final String content;

    @ApiParam(value = "댓글이 작성된 전단지 아이디", required = true, example = "1")
    @Schema(description = "댓글이 작성된 전단지 아이디")
    private final Long postId;

    @ApiParam(value = "댓글이 작성된 전단지 제목", required = true, example = "OOO 구합니다!")
    @Schema(description = "댓글이 작성된 전단지 제목")
    private final String post_title;

    @Builder
    public MyInfoCommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.post_title = comment.getPost().getTitle();
    }
}
