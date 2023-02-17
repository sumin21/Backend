package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "고민이 있어요")
    @NotNull
    private String title;
    @ApiModelProperty(value = "테스트 내용")
    @NotNull
    private String content;
    @ApiModelProperty(value = "kakaotalk")
    @NotNull
    private String contactWay;
    @ApiModelProperty(value = "http://open.kakao.com/o/sDMnCBS")
    @NotNull
    private String contactUrl;
    @ApiModelProperty(value = "공부")
    @NotNull
    private String categoryKeyword;
    @ApiModelProperty(value = "스프링,자바,프로젝트")
    private String tags;


    @Builder
    public PostRequestDto(String title, String content, String contactWay, String contactUrl, String categoryKeyword, String tags) {
        this.title = title;
        this.content = content;
        this.contactWay = contactWay;
        this.contactUrl = contactUrl;
        this.categoryKeyword = categoryKeyword;
        this.tags = tags;
    }

    public Post toEntity() {
        Post post = new Post();
        post.create(title, content, contactWay, contactUrl, categoryKeyword, tags);
        return post;
    }
}
