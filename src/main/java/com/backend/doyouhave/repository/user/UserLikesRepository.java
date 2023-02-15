package com.backend.doyouhave.repository.user;

import com.backend.doyouhave.domain.user.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
}
