package com.backend.doyouhave.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;

    public PostResponseDto(Long postId) {
        this.postId = postId;
    }
}
