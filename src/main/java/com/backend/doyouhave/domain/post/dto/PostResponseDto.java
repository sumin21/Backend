package com.backend.doyouhave.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    @ApiModelProperty(value = "1")
    private Long postId;

    public PostResponseDto(Long postId) {
        this.postId = postId;
    }
}
