package com.bondspace.service;

import com.bondspace.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Create a new user
    User createUser(String emailAddress, String password, String firstName, String lastName);

    // Find a user by ID
    Optional<User> findUserById(int id);

    // Find a user by email address
    Optional<User> findUserByEmailAddress(String emailAddress);

    // Find a user by name
    Optional<User> findUserByUsername(String firstName, String lastName);

    // Update user details
    User updateUser(int id, String firstName, String lastName, String userBio, String userPicture);

    // Change user password
    boolean updatePassword(int id, String newPassword);

    // Change user email address
    boolean updateEmailAddress(int id, String newEmailAddress);

    // Delete a user
    boolean deleteUser(int id);

    // Retrieve all users (admin functionality)
    List<User> getAllUsers();

    // Add a user to a space
    boolean addUserToSpace(int userId, int spaceId);

    // Remove a user from a space
    boolean removeUserFromSpace(int userId, int spaceId);

}
