package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateResponseDto {

    @ApiModelProperty(value = "고민이 있어요")
    private String title;
    @ApiModelProperty(value = "테스트")
    private String content;
    @ApiModelProperty(value = "Google Form")
    private String contactWay;
    @ApiModelProperty(value = "http://open.kakao.com/o/sDMnCBS")
    private String contactUrl;
    @ApiModelProperty(value = "MEDICAL")
    private String categoryKeyword;
    @ApiModelProperty(value = "['test1', 'test2', 'test3']")
    private List<String> tags;

    private String img;

    private String imgSecond;

    @Builder
    public PostUpdateResponseDto(Post entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.contactWay = entity.getContactWay();
        this.contactUrl = entity.getContactUrl();
        this.categoryKeyword = entity.getCategory();
        List<String> entityTags = Arrays.stream(entity.getTags().split(",")).toList();
        this.tags = entityTags;
        this.img = entity.getImg();
        this.imgSecond = entity.getImgSecond();
    }
}
