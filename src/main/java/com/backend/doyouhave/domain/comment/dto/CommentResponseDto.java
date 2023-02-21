package com.backend.doyouhave.domain.comment.dto;

import com.backend.doyouhave.domain.comment.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Objects;

@Getter
@Schema(description = "CommentResponseDTO")
@ApiModel(description = "CommentResponseDTO")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponseDto {

    @ApiModelProperty(value = "해당 유저가 게시글 작성자인지 유무", required = true, example = "true")
    @Schema(description = "해당 유저가 게시글 작성자인지 유무")
    private Boolean isWriter;

    @ApiModelProperty(value = "댓글 목록 (페이징)", required = true)
    @Schema(description = "댓글 목록 (페이징)")
    private Page<CommentInfoDto> comments;

    @Builder
    public CommentResponseDto(boolean isWriter, Page<CommentInfoDto> comments) {
        this.isWriter = isWriter;
        this.comments = comments;
    }

    public static CommentResponseDto from(boolean isWriter, Page<CommentInfoDto> comments) {
        return CommentResponseDto.builder()
                .isWriter(isWriter)
                .comments(comments)
                .build();
    }
}
