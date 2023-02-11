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

    /* 최근 작성일 기준 */
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByCategoryAndTagsContaining(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);
    Page<Post> findByCategoryOrTagsContaining(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);

    /* 조회수 기준 */
    Page<Post> findAllByOrderByViewCountDesc(Pageable pageable);
    Page<Post> findByCategoryAndTagsContainingOrderByViewCountDesc(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);
    Page<Post> findByCategoryOrTagsContainingOrderByViewCountDesc(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);

    /* 댓글 수 기준 */
    Page<Post> findAllByOrderByCommentNumDesc(Pageable pageable);
    Page<Post> findByCategoryAndTagsContainingOrderByCommentNumDesc(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);
    Page<Post> findByCategoryOrTagsContainingOrderByCommentNumDesc(@Param("keyword") String keyword, @Param("tag") String tag, Pageable pageable);

}
