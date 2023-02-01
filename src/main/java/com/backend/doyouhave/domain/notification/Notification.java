package com.backend.doyouhave.domain.notification;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class Notification {

    private String post_title;

    private String comment_content;

    private LocalDateTime notifiedDate;

    protected Notification() {

    }

    public void create(String post_title, String comment_content) {
        this.post_title = post_title;
        this.comment_content = comment_content;
        this.notifiedDate = LocalDateTime.now();
    }
}
