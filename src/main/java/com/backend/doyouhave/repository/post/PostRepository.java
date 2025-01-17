package com.backend.doyouhave.repository.post;

import com.backend.doyouhave.domain.post.Post;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /* 카테고리, 태그 및 정렬별 전단지 조회  */
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByCategoryAndTagsContaining(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);
    Page<Post> findByCategoryOrTagsContaining(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);

    /* 전단지 검색 조회 */
    Page<Post> findByCategoryAndTitleContainingOrContentContaining(@Param("keyword") String keyword, @Param("search") String title, @Param("search") String content, Pageable pageable);

    Page<Post> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /* 북마크한 전단지 조회 */
    @Query(value = "select * from Post p left join user_likes u on p.post_id = u.post_id where u.user_id = :userId order by u.created_date desc", nativeQuery = true)
    Page<Post> findMarkedPostByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "select * from Post p where p.reported_count > 0", nativeQuery = true)
    Page<Post> findAllByReportedPost(Pageable pageable);
}
