package com.backend.doyouhave.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String code;
    private String msg;

    public PostResponseDto(Long postId, String code, String msg) {
        this.postId = postId;
        this.code = code;
        this.msg = msg;
    }
}
