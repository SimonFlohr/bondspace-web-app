package com.bondspace.repository;

import com.bondspace.domain.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory, Integer> {
    @Query("SELECT m FROM Memory m WHERE m.space.id = :spaceId")
    List<Memory> findBySpaceId(@Param("spaceId") Integer spaceId);
}
