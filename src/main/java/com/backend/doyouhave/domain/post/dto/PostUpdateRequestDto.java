package com.backend.doyouhave.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String contactWay;
    @NotNull
    private String categoryKeyword;

    private String tags;
}
