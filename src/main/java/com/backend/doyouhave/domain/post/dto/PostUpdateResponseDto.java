package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateResponseDto {

    private String title;

    private String content;

    private String contactWay;

    private String categoryKeyword;

    private List<String> tags;

    private String img;

    private String imgSecond;

    @Builder
    public PostUpdateResponseDto(Post entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.contactWay = entity.getContactWay();
        this.categoryKeyword = entity.getCategory();
        List<String> entityTags = Arrays.stream(entity.getTags().split(",")).toList();
        this.tags = entityTags;
        this.img = entity.getImg();
        this.imgSecond = entity.getImgSecond();
    }
}
