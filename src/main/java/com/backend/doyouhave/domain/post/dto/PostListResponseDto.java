package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostListResponseDto {
    @ApiParam(value = "글 제목", required = true, example = "Spring")
    private String title;
    @ApiParam(value = "글 카테고리", required = true, example = "STUDY")
    private String categoryKeyword;
    @ApiParam(value = "글 태그", example = "['MVC', 'SECURITY', 'MYSQL']")
    private List<String> tags;

    public PostListResponseDto(Post entity) {
        this.title = entity.getTitle();
        this.categoryKeyword = entity.getCategory();
        List<String> entityTags = Arrays.stream(entity.getTags().split(",")).toList();
        this.tags = entityTags;
    }
}
