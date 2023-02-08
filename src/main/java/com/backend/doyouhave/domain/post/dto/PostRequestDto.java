package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String contactWay;
    @NotNull
    private String categoryKeyword;

    private String tags;


    @Builder
    public PostRequestDto(String title, String content, String contactWay, String categoryKeyword, String tags) {
        this.title = title;
        this.content = content;
        this.contactWay = contactWay;
        this.categoryKeyword = categoryKeyword;
        this.tags = tags;
    }

    public Post toEntity() {
        Post post = new Post();
        post.create(title, content, contactWay, categoryKeyword, tags);
        return post;
    }
}
