package com.backend.doyouhave.repository.post;

import com.backend.doyouhave.domain.post.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByKeyword(String keyword);

    // 전체 전단지 중 인기 태그 TOP5 조회
    @Query(value = "select tags from category, category_tags group by tags order by count(tags) desc limit 5", nativeQuery = true)
    List<String> findTop5ByTags();

    // 카테고리별 인기 태그 TOP5 조회
    @Query(value = "select tags from category c, category_tags where c.keyword = :keyword and c.category_id = category_category_id group by tags order by count(tags) desc limit 5", nativeQuery = true)
    List<String> findTop5ByCategoryAndTags(@Param("keyword") String keyword);
}
