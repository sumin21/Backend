package com.backend.doyouhave.repository.resign;

import com.backend.doyouhave.domain.resign.ResignReason;
import com.backend.doyouhave.domain.user.Role;
import com.backend.doyouhave.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResignReasonRepository extends JpaRepository<ResignReason, Long> {
    Optional<ResignReason> findByReason(String reason);
}
