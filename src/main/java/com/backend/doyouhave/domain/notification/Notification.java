package com.backend.doyouhave.domain.notification;

import com.backend.doyouhave.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false)
    private LocalDateTime notifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void create(String postTitle, String commentContent) {
        this.postTitle = postTitle;
        this.commentContent = commentContent;
        this.notifiedDate = LocalDateTime.now();
    }
}
