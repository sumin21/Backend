package com.backend.doyouhave.repository.user;

import com.backend.doyouhave.domain.post.Post;
import com.backend.doyouhave.domain.user.User;
import com.backend.doyouhave.domain.user.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
    Optional<UserLikes> findByUserIdAndPostId(Long userId, Long postId);
}
