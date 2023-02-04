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
    private String contactWay;

    @Column(nullable = false)
    private String category;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private String img;

    private String imgSecond;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public void create(String title, String content, String contactWay, String category, List<String> tags, String img, String imgSecond) {
        this.title = title;
        this.content = content;
        this.contactWay = contactWay;
        this.category = category;
        this.tags = tags;
        this.img = img;
        this.imgSecond = imgSecond;
    }
 }
