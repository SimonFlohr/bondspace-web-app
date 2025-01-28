package com.bondspace.repository;

import com.bondspace.domain.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpaceRepository extends JpaRepository<Space, Integer> {
    @Query("SELECT s FROM Space s WHERE s.id = :spaceId")
    Space findByIdSimple(@Param("spaceId") Integer spaceId);
}
