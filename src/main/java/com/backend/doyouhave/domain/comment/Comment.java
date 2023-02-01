package com.backend.doyouhave.domain.comment;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    protected Comment() {

    }

    public void create(String content) {
        this.content = content;
        this.createdTime = LocalDateTime.now();
    }

    public void update(String content) {
        this.content = content;
        this.modifiedTime = LocalDateTime.now();
    }

}
