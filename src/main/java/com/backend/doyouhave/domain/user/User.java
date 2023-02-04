package com.backend.doyouhave.domain.user;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Long socialId;

    private String email;

    private String img;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public static User createKakaoUser(Long kakaoId, String email, String img, String nickname) {
        return User.builder()
                .socialId(kakaoId)
                .email(email)
                .img(img)
                .nickname(nickname)
                .role(Role.KAKAO)
                .build();
    }

    public static User createNaverUser(Long naverId, String email, String img, String nickname) {
        return User.builder()
                .socialId(naverId)
                .email(email)
                .img(img)
                .nickname(nickname)
                .role(Role.NAVER)
                .build();
    }
}