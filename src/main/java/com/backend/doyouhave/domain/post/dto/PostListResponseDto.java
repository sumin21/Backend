package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostListResponseDto {
    @ApiParam(value = "글 제목", required = true, example = "Spring")
    private String title;
    @ApiParam(value = "글 카테고리", required = true, example = "STUDY")
    private String categoryKeyword;
    @ApiParam(value = "글 태그", example = "['MVC', 'SECURITY', 'MYSQL']")
    private List<String> tags;
    @ApiParam(value = "북마크한 회원 ID")
    private List<Long> marks = new ArrayList<>();

    public PostListResponseDto(Post entity) {
        this.title = entity.getTitle();
        this.categoryKeyword = entity.getCategory();
        List<String> entityTags = Arrays.stream(entity.getTags().split(",")).toList();
        this.tags = entityTags;
        this.marks = entity.getUserLikes().stream().map(user -> user.getId()).collect(Collectors.toList());
    }
}
