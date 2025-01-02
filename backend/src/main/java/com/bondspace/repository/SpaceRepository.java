package com.bondspace.repository;

import com.bondspace.domain.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Integer> {
}
