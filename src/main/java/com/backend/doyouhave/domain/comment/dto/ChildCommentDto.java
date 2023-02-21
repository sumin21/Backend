package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Schema(description = "ChildCommentDto")
@ApiModel(description = "ChildCommentDto")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChildCommentDto {

    @ApiModelProperty(value = "댓글 아이디", required = true, example = "1")
    @Schema(description = "댓글 아이디")
    private Long commentId;

    @ApiModelProperty(value = "댓글 생성 날짜", required = true, example = "2022~")
    @Schema(description = "댓글 생성 날짜")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    @ApiModelProperty(value = "작성자 별명", required = true, example = "익명1")
    @Schema(description = "작성자 별명")
    private String name;

    @ApiModelProperty(value = "댓글 내용", required = true, example = "댓글 내용 예시")
    @Schema(description = "댓글 내용")
    private String content;

    @ApiModelProperty(value = "삭제되었는지 여부", required = true, example = "true")
    @Schema(description = "삭제 여부", required = true)
    private Boolean isRemoved;

    @ApiModelProperty(value = "비밀댓글인지 여부", required = true, example = "true")
    @Schema(description = "비밀댓글 여부", required = true)
    private Boolean isSecret;

    @ApiModelProperty(value = "댓글 작성자 유무", required = true, example = "true")
    @Schema(description = "댓글 작성자 유무", required = true)
    private Boolean isCommentWriter;

    @Builder
    public ChildCommentDto(Comment comment, Long userId, String name, Boolean isParentCommentWriter) {
        this.commentId = comment.getId();
        this.createdDate = comment.getCreatedDate();
        this.name = name;
        this.isRemoved = comment.isRemoved();
        this.isSecret = comment.isSecret();
        this.isCommentWriter = userId != null && Objects.equals(comment.getUser().getId(), userId); // 댓글 작성자 id
        boolean isWriter = userId != null && Objects.equals(comment.getPost().getUser().getId(), userId); // 글쓴이 id

        this.content = comment.getContent();
        if (comment.isRemoved()) this.content = "삭제된 댓글입니다.";
        else if (comment.isSecret() && !isWriter && !isCommentWriter && !isParentCommentWriter) this.content = "비밀 댓글입니다.";
    }

    public static ChildCommentDto from(Comment comment, Long userId, String name, Boolean isParentCommentWriter) {
        return ChildCommentDto.builder()
                .comment(comment)
                .userId(userId)
                .name(name)
                .isParentCommentWriter(isParentCommentWriter)
                .build();
    }
}