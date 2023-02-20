package com.backend.doyouhave.repository.comment;

import com.backend.doyouhave.domain.comment.Comment;
import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByUser(User user, Pageable pageable);

    List<Comment> findByUser(User user);
    List<Comment> findByPost(Post post);
    List<Comment> findByPostOrderByCreatedDate(Post post);
}
