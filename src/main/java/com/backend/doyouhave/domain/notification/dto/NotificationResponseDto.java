package com.backend.doyouhave.domain.notification.dto;

import com.backend.doyouhave.domain.notification.Notification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class NotificationResponseDto {
    @ApiModelProperty(value = "2")
    private Long notificationId;
    @ApiModelProperty(value = "글 제목")
    private String postTitle;
    @ApiModelProperty(value = "댓글 내용")
    private String commentContent;
    @ApiModelProperty(value = "2023-03-01")
    private String notifiedDate;

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.postTitle = notification.getPostTitle();
        this.commentContent = notification.getCommentContent();
        this.notifiedDate = notification.getNotifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
