package com.backend.doyouhave.domain.post.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    @ApiModelProperty(value = "제목")
    @NotNull
    private String title;
    @ApiModelProperty(value = "내용")
    @NotNull
    private String content;
    @ApiModelProperty(value = "카카오톡")
    @NotNull
    private String contactWay;
    @ApiModelProperty(value = "의류")
    @NotNull
    private String categoryKeyword;
    @ApiModelProperty(value = "나이키,아디다스")
    private String tags;
}
