package com.bondspace.controller;

import com.bondspace.domain.dto.CreateSpaceRequestDTO;
import com.bondspace.domain.dto.InviteUserRequestDTO;
import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.User;
import com.bondspace.domain.model.UserNotification;
import com.bondspace.domain.model.UserSpace;
import com.bondspace.domain.model.enums.SpaceUserRole;
import com.bondspace.repository.SpaceRepository;
import com.bondspace.repository.UserNotificationRepository;
import com.bondspace.repository.UserRepository;
import com.bondspace.repository.UserSpaceRepository;
import com.bondspace.util.SessionUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createSpace(@RequestBody CreateSpaceRequestDTO request) {
        // Get the current user's ID from the session
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            // Create and save the new space
            Space space = new Space(request.getSpaceName(), request.getSpaceDescription());
            space = spaceRepository.save(space);

            // Create and save the UserSpace relationship
            UserSpace userSpace = new UserSpace(
                    SpaceUserRole.OWNER,
                    userRepository.findById(userId).orElseThrow(),
                    space
            );
            userSpaceRepository.save(userSpace);

            return ResponseEntity.ok(Map.of("message", "Space created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to create space: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{spaceId}")
    public ResponseEntity<?> deleteSpace(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User not authenticated"));
        }

        try {
            // Find the UserSpace entry for this user in this space
            List<UserSpace> userSpaces = userSpaceRepository.findAllBySpaceId(spaceId);
            UserSpace userSpace = userSpaces.stream()
                    .filter(us -> us.getUser().getId() == userId)  // Changed from .equals() to ==
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("User is not a member of this space"));

            // Check if the user is the OWNER
            if (userSpace.getUserRole() != SpaceUserRole.OWNER) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Only the owner can delete a space"));
            }

            // Delete the space (cascading will handle related entities)
            spaceRepository.deleteById(spaceId);

            return ResponseEntity.ok(Map.of("message", "Space deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to delete space: " + e.getMessage()));
        }
    }

    @GetMapping("/user-spaces")
    public ResponseEntity<?> getUserSpaces() {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            List<UserSpace> userSpaces = userSpaceRepository.findAllByUserId(userId);
            
            List<Map<String, Object>> spacesResponse = userSpaces.stream()
                    .filter(userSpace -> userSpace.getUserRole() != SpaceUserRole.INVITEE) // Add this line
                    .map(userSpace -> {
                        Space space = userSpace.getSpace();
                        Map<String, Object> spaceData = new HashMap<>();
                        spaceData.put("id", space.getId());
                        spaceData.put("spaceName", space.getSpaceName());
                        spaceData.put("spaceDescription", space.getSpaceDescription());
                        return spaceData;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(spacesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch spaces: " + e.getMessage()));
        }
    }

    @GetMapping("/{spaceId}")
    public ResponseEntity<?> getSpaceDetails(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            Space space = spaceRepository.findByIdSimple(spaceId);
            if (space == null) {
                return ResponseEntity.notFound().build();
            }

            // Create a simplified response object
            Map<String, Object> spaceDetails = new HashMap<>();
            spaceDetails.put("id", space.getId());
            spaceDetails.put("spaceName", space.getSpaceName());
            spaceDetails.put("spaceDescription", space.getSpaceDescription());
            spaceDetails.put("createdAt", space.getCreatedAt());

            return ResponseEntity.ok(spaceDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch space details: " + e.getMessage()));
        }
    }

    @GetMapping("/{spaceId}/members")
    public ResponseEntity<?> getSpaceMembers(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            List<UserSpace> userSpaces = userSpaceRepository.findAllBySpaceId(spaceId);
            List<Map<String, Object>> members = userSpaces.stream()
                    .map(userSpace -> {
                        Map<String, Object> member = new HashMap<>();
                        member.put("firstName", userSpace.getUser().getFirstName());
                        member.put("lastName", userSpace.getUser().getLastName());
                        member.put("role", userSpace.getUserRole().toString());
                        return member;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch members: " + e.getMessage()));
        }
    }

    @GetMapping("/{spaceId}/memories")
    public ResponseEntity<?> getSpaceMemories(@PathVariable int spaceId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            Space space = spaceRepository.findById(spaceId).orElseThrow();
            return ResponseEntity.ok(space.getMemories());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch memories: " + e.getMessage()));
        }
    }

    @PostMapping("/{spaceId}/invite")
    @Transactional
    public ResponseEntity<?> inviteUser(@PathVariable int spaceId, @RequestBody InviteUserRequestDTO request) {
        Integer currentUserId = sessionUtil.getLoggedInUserId();
        if (currentUserId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
        }

        try {
            // Find the invitee by email
            User invitee = userRepository.findByEmailAddress(request.getEmailAddress())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Space space = spaceRepository.findById(spaceId)
                    .orElseThrow(() -> new IllegalArgumentException("Space not found"));

            // Check if user is already an INVITEE
            List<UserSpace> existingUserSpaces = userSpaceRepository.findAllBySpaceId(spaceId);
            boolean isAlreadyInvited = existingUserSpaces.stream()
                    .anyMatch(userSpace ->
                            userSpace.getUser().getId() == invitee.getId() &&
                                    userSpace.getUserRole() == SpaceUserRole.INVITEE
                    );

            if (isAlreadyInvited) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "User has already been invited to this space"));
            }

            // Rest of your existing code...
            UserNotification notification = new UserNotification(
                    "SPACE_INVITE",
                    String.format("You have been invited to join the space: %s", space.getSpaceName()),
                    invitee
            );

            // Save the notification first to get its ID
            notification = userNotificationRepository.save(notification);

            // Get current notifications array or create new one if null
            Integer[] currentNotifications = invitee.getNotifications();
            Integer[] newNotifications;

            if (currentNotifications == null) {
                newNotifications = new Integer[]{notification.getId()};
            } else {
                newNotifications = Arrays.copyOf(currentNotifications, currentNotifications.length + 1);
                newNotifications[newNotifications.length - 1] = notification.getId();
            }

            // Update the notifications array
            invitee.setNotifications(newNotifications);

            // Save both entities
            userRepository.save(invitee);

            UserSpace userSpace = new UserSpace(
                    SpaceUserRole.INVITEE,
                    invitee,
                    space
            );
            userSpaceRepository.save(userSpace);

            return ResponseEntity.ok(Map.of("message", "Invitation sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to send invitation: " + e.getMessage()));
        }
    }
}