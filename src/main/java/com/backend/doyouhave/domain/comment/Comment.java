package com.backend.doyouhave.domain.comment;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean isRemoved = false;

    @Column
    private boolean isSecret = false;

    public void createParent(User user, Post post, String content, boolean isSecret) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.isSecret = isSecret;
    }

    public void createChild(User user, Post post, String content, Comment parent, boolean isSecret) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.parent = parent;
        this.isSecret = isSecret;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.isRemoved = true;
    }

    public void conversion() {
        this.isSecret = true;
    }
}
