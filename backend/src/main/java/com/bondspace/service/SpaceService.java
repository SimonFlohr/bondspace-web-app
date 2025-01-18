package com.bondspace.service;

import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.User;

import java.util.List;

public interface SpaceService {

    // Create a new space
    Space createSpace(String spaceName, String spaceDescription, int userId);

    // Get space details by ID
    Space getSpaceById(int spaceId);

    // Update an existing space
    Space updateSpace(int spaceId, String spaceDescription, String spacePicture);

    // Add a new users to space
    boolean addUsersToSpace(int spaceId, List<Integer> userIds, String role);

    // Delete a space by ID
    boolean deleteSpace(int spaceId);

    // Get all spaces for a user
    List<Space> getAllSpacesForUser(int userId);

    // Get all users from a space
    List<User> getAllUsersFromSpace(int spaceId);

    // Check if a user is in a space
    boolean isUserInSpace(int spaceId, int userId);

}
