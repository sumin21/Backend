package com.backend.doyouhave.domain.user;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.notification.Notification;
import com.backend.doyouhave.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(
        name="Users",
        uniqueConstraints={
                @UniqueConstraint(
                        columnNames={"social_id", "role"}
                )
        }
)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "social_id", nullable = false)
    private String socialId;

    private String email;

    private String img;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "refresh_token")
    private String refreshToken;

    // 최근 접속일자 (로그인시 LocalDateTime.now()로 업데이트)
    private LocalDateTime recentDate;

    // 회원 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserState userState;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();
    public void setNotification(Notification notification) {
        this.notifications.add(notification);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLikes> userLikes = new ArrayList<>();

    public static User createKakaoUser(String kakaoId, String email, String img, String nickname) {
        return User.builder()
                .socialId(kakaoId)
                .email(email)
                .img(img)
                .nickname(nickname)
                .role(Role.KAKAO)
                .userState(UserState.NORMAL)
                .build();
    }

    public static User createNaverUser(String naverId, String email, String img, String nickname) {
        return User.builder()
                .socialId(naverId)
                .email(email)
                .img(img)
                .nickname(nickname)
                .role(Role.NAVER)
                .userState(UserState.NORMAL)
                .build();
    }
}