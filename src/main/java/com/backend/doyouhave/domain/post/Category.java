package com.backend.doyouhave.domain.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private int count;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @Builder
    public Category(String keyword, int count, List<String> tags) {
        this.keyword = keyword;
        this.count = count;
        this.tags = tags;
    }
}
