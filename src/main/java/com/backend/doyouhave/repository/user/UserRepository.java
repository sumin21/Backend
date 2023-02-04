package com.backend.doyouhave.repository.user;

import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRoleAndSocialId(Role role, Long socialId);

    @Query(value = "SELECT u.refresh_token FROM Users u WHERE u.user_id = :id", nativeQuery = true)
    String findRefreshTokenById(@Param("id") Long userId);
}
