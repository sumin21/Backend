package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PostRequestDto {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String contactWay;

    private String img;

    @Builder
    public PostRequestDto(String title, String content, String img, String contactWay) {
        this.title = title;
        this.content = content;
        this.contactWay = contactWay;
        this.img = img;
    }

    public Post toEntity() {
        Post post = new Post();
        post.create(title, content, img, contactWay);
        return post;
    }
}
