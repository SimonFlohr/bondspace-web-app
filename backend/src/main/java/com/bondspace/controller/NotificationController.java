package com.bondspace.controller;

import com.bondspace.domain.model.User;
import com.bondspace.domain.model.UserSpace;
import com.bondspace.domain.model.enums.SpaceUserRole;
import com.bondspace.repository.UserNotificationRepository;
import com.bondspace.repository.UserRepository;
import com.bondspace.repository.UserSpaceRepository;
import com.bondspace.util.SessionUtil;
import com.bondspace.domain.model.UserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping("/user")
    public ResponseEntity<?> getUserNotifications() {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User not authenticated"));
        }

        try {
            List<UserNotification> notifications = userNotificationRepository.findAllByUserId(userId);

            // Convert to DTOs to avoid serialization issues
            List<Map<String, Object>> notificationDTOs = notifications.stream()
                    .map(notification -> {
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("id", notification.getId());
                        dto.put("userNotificationType", notification.getUserNotificationType());
                        dto.put("message", notification.getMessage());
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(notificationDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to fetch notifications: " + e.getMessage()));
        }
    }

    @PostMapping("/{notificationId}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable int notificationId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User not authenticated"));
        }

        try {
            // Get the notification
            UserNotification notification = userNotificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

            // Get the user
            User user = notification.getUser();

            // Find the UserSpace entry where the user is an INVITEE
            List<UserSpace> userSpaces = userSpaceRepository.findAllByUserId(userId);
            UserSpace inviteeSpace = userSpaces.stream()
                    .filter(userSpace ->
                            userSpace.getUserRole() == SpaceUserRole.INVITEE)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No pending invitation found"));

            // Update the role from INVITEE to MEMBER
            inviteeSpace.setUserRole(SpaceUserRole.MEMBER);
            userSpaceRepository.save(inviteeSpace);

            // Remove notification ID from user's notifications array
            removeNotificationFromUser(user, notificationId);

            // Delete the notification
            userNotificationRepository.deleteById(notificationId);

            return ResponseEntity.ok(Map.of("message", "Invitation accepted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to accept invitation: " + e.getMessage()));
        }
    }

    @PostMapping("/{notificationId}/deny")
    public ResponseEntity<?> denyInvitation(@PathVariable int notificationId) {
        Integer userId = sessionUtil.getLoggedInUserId();
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "User not authenticated"));
        }

        try {
            // Get the notification
            UserNotification notification = userNotificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

            // Get the user
            User user = notification.getUser();

            // Find and delete the UserSpace entry where the user is an INVITEE
            List<UserSpace> userSpaces = userSpaceRepository.findAllByUserId(userId);
            UserSpace inviteeSpace = userSpaces.stream()
                    .filter(userSpace ->
                            userSpace.getUserRole() == SpaceUserRole.INVITEE)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No pending invitation found"));

            // Delete the UserSpace entry
            userSpaceRepository.delete(inviteeSpace);

            // Remove notification ID from user's notifications array
            removeNotificationFromUser(user, notificationId);

            // Delete the notification
            userNotificationRepository.deleteById(notificationId);

            return ResponseEntity.ok(Map.of("message", "Invitation denied successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Failed to deny invitation: " + e.getMessage()));
        }
    }

    private void removeNotificationFromUser(User user, int notificationId) {
        Integer[] currentNotifications = user.getNotifications();
        if (currentNotifications != null) {
            // Create new array without the specified notification ID
            Integer[] newNotifications = Arrays.stream(currentNotifications)
                    .filter(id -> id != notificationId)
                    .toArray(Integer[]::new);

            user.setNotifications(newNotifications);
            userRepository.save(user);
        }
    }
}