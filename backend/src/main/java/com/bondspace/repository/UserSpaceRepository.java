package com.bondspace.repository;

import com.bondspace.domain.model.UserSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSpaceRepository extends JpaRepository<UserSpace, Integer> {
    @Query("SELECT us FROM UserSpace us JOIN FETCH us.space WHERE us.user.id = :userId")
    List<UserSpace> findAllByUserId(@Param("userId") Integer userId);

    @Query("SELECT us FROM UserSpace us JOIN FETCH us.user WHERE us.space.id = :spaceId")
    List<UserSpace> findAllBySpaceId(@Param("spaceId") Integer spaceId);
}