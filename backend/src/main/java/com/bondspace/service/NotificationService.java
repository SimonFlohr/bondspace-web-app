package com.bondspace.service;

import com.bondspace.domain.model.Notification;

import java.util.List;

public interface NotificationService {

    // Create a new notification
    Notification createNotification(Notification notification);

    // Retrieve a notification by ID
    Notification getNotification(int notificationId);

    // Retrieve all notifications for a specific user
    List<Notification> getAllNotificationsForUser(int userId);

    // Retrieve all notifications for a specific space
    List<Notification> getAllNotificationsForSpace(int spaceId);

    // Update the details of an existing notification
    Notification updateNotification(int notificationId, Notification updatedNotification);

    // Delete a notification by ID
    boolean deleteNotification(int notificationId);

    // Send a notification to recipients
    boolean sendNotification(Notification notification);

}
