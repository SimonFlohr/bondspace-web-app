package com.bondspace.repository;

import com.bondspace.domain.model.UserSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSpaceRepository extends JpaRepository<UserSpace, Integer> {
}
