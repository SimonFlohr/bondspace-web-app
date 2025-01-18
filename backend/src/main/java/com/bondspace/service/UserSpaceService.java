package com.bondspace.service;

import com.bondspace.domain.model.UserSpace;
import com.bondspace.domain.model.enums.SpaceUserRole;

import java.util.List;

public interface UserSpaceService {

    // Add a user to a space
    boolean addUserToSpace(int userId, int spaceId, SpaceUserRole role);

    // Remove a user from a space
    boolean removeUserFromSpace(int userId, int spaceId);

    // Find all users within a space
    List<UserSpace> findAllUsersBySpaceId(int spaceId);

    // Find all spaces for a user
    List<UserSpace> findAllSpacesByUserId(int userId);

    // Update the user role for a user within a space
    boolean updateUserRoleInSpace(int userId, int spaceId, SpaceUserRole role);

}