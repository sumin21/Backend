package com.backend.doyouhave.domain.notification.dto;

import com.backend.doyouhave.domain.notification.Notification;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class NotificationResponseDto {
    private Long notificationId;
    private String postTitle;
    private String commentContent;
    private String notifiedDate;

    public NotificationResponseDto(Notification notification) {
        this.notificationId = notification.getId();
        this.postTitle = notification.getPostTitle();
        this.commentContent = notification.getCommentContent();
        this.notifiedDate = notification.getNotifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
