package com.backend.doyouhave.domain.post;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.post.dto.PostUpdateRequestDto;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.UserLikes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Post extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String contactWay;

    @Column(nullable = false)
    private String contactUrl;

    @Column(nullable = false)
    private String category;

    private String tags;

    private String img;

    private String imgSecond;

    private long viewCount;

    // 댓글 수 기준 정렬시 사용(성능 측면에서 조인보다 필드 정렬이 효율적)
    private long commentNum;

    // 신고 횟수
    private long reportedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<UserLikes> userLikes = new ArrayList<>();

    public void create(String title, String content, String contactWay, String contactUrl, String category, String tags) {
        this.title = title;
        this.content = content;
        this.contactWay = contactWay;
        this.contactUrl = contactUrl;
        this.category = category;
        this.tags = tags;
        this.viewCount = 0; // 전단지 첫 생성시 조회수 0으로 초기화, 상세정보 클릭시 카운팅
        this.reportedCount = 0; // 전단지 첫 생성시 신고 횟수 0으로 초기화
        this.commentNum = this.getCommentList().size();
    }

    public void update(PostUpdateRequestDto entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.contactWay = entity.getContactWay();
        this.contactUrl = entity.getContactUrl();
        this.category = entity.getCategoryKeyword();
        this.tags = entity.getTags();
    }

    public void setUser(User user) {
        user.getPosts().add(this);
        this.user = user;
    }

    public void remove(User user) {
        user.getPosts().remove(this);
    }
 }
