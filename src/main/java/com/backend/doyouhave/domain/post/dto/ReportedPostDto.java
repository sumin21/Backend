package com.backend.doyouhave.domain.post.dto;

import com.backend.doyouhave.domain.post.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ReportedPostDto {

    @ApiModelProperty(value = "전단지 아이디")
    @NotBlank
    private Long postId;

    @ApiModelProperty(value = "전단지 제목")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "전단지 작성자 이메일")
    @NotBlank
    private String writerEmail;

    public ReportedPostDto(Post reportedPost) {
        this.postId = reportedPost.getId();
        this.title = reportedPost.getTitle();
        this.writerEmail = reportedPost.getUser().getEmail();
    }
}
