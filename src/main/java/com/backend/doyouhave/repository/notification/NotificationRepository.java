package com.backend.doyouhave.repository.notification;

import com.backend.doyouhave.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop8ByUserIdOrderByNotifiedDateDesc(Long userId);
}
