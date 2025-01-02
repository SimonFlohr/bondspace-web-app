package com.bondspace.repository;

import com.bondspace.domain.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Integer> {
}
