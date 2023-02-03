package com.backend.doyouhave.repository.user;

import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRoleAndSocialId(Role role, Long socialId);
}
