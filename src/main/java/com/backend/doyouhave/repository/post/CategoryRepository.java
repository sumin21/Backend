package com.backend.doyouhave.repository.post;

import com.backend.doyouhave.domain.post.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByKeyword(String keyword);
}
