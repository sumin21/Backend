package com.backend.doyouhave.repository.comment;

import com.backend.doyouhave.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
