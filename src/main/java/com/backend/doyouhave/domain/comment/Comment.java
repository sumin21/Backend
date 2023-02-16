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

    @Column
    private Long parentId;

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

    public void  create(User user, Post post, String content, Long parentId, boolean isSecret) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.parentId = parentId;
        this.isSecret = isSecret;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.isRemoved = true;
        this.getPost().getCommentList().remove(this);
        this.getPost().setCommentNum(this.getPost().getCommentList().size() - 1);
    }

    public void conversion() {
        this.isSecret = true;
    }

    public void setPost(Post post) {
        post.getCommentList().add(this);
        post.setCommentNum(post.getCommentList().size() + 1);
        this.post = post;
    }
}
