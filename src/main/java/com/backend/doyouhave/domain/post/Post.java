package com.backend.doyouhave.domain.post;

import com.backend.doyouhave.domain.BaseTimeEntity;
import com.backend.doyouhave.domain.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    private String img;

    @Column(nullable = false)
    private String contactWay;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    // private Category category; 태그 포함(String[])

    public void create(String title, String content, String img, String contactWay) {
        this.title = title;
        this.content = content;
        this.img = img;
        this.contactWay = contactWay;
    }
 }
