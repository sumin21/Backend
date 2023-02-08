package com.backend.doyouhave.domain.post;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.post.dto.PostUpdateRequestDto;
import com.backend.doyouhave.domain.user.User;
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
    private String category;

    private String tags;

    private String img;

    private String imgSecond;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public void create(String title, String content, String contactWay, String category, String tags) {
        this.title = title;
        this.content = content;
        this.contactWay = contactWay;
        this.category = category;
        this.tags = tags;
    }

    public void update(PostUpdateRequestDto entity) {
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.contactWay = entity.getContactWay();
        this.category = entity.getCategoryKeyword();
        this.tags = entity.getTags();
    }

    public void setUser(User user) {
        user.getPosts().add(this);
        this.user = user;
    }
 }
