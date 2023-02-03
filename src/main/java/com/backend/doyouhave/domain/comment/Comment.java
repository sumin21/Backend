package com.backend.doyouhave.domain.comment;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void create(String content) {
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

}
