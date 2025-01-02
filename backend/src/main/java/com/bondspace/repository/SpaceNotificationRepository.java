package com.bondspace.repository;

import com.bondspace.domain.model.SpaceNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceNotificationRepository extends JpaRepository<SpaceNotification, Integer> {
}
