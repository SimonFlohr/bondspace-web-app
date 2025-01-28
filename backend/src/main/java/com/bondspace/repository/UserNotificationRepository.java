package com.bondspace.repository;

import com.bondspace.domain.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Integer> {
    @Query("SELECT un FROM UserNotification un JOIN FETCH un.user WHERE un.user.id = :userId")
    List<UserNotification> findAllByUserId(@Param("userId") Integer userId);
}
