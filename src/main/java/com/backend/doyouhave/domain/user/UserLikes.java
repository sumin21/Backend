package com.backend.doyouhave.domain.user;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserLikes extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void setUser(User user) {
        user.getUserLikes().add(this);
        this.user = user;
    }

    public void setPost(Post post) {
        post.getUserLikes().add(this);
        this.post = post;
    }

    public void deleteUserAndPost(User markedUser, Post markedPost) {
        markedUser.getUserLikes().remove(this);
        markedPost.getUserLikes().remove(this);
    }

    @Builder
    public UserLikes(User markedUser, Post markedPost) {
        this.user = markedUser;
        this.post = markedPost;
    }
}
