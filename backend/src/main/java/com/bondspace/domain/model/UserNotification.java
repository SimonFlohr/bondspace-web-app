package com.bondspace.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_notifications")
public class UserNotification implements Notification {

    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_notification_type", nullable = false)
    private String userNotificationType;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // CONSTRUCTORS
    public UserNotification() {};

    public UserNotification(String userNotificationType, String message, User user) {
        this.userNotificationType = userNotificationType;
        this.message = message;
        this.user = user;
    }

    // GETTERS & SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserNotificationType() {
        return userNotificationType;
    }

    public void setUserNotificationType(String userNotificationType) {
        this.userNotificationType = userNotificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // METHODS
    public String buildNotification() {
        return "";
    }

    public boolean addToStream() {
        return false;
    }

    public void setUserWithBackReference(User user) {
        this.user = user;
        if (user != null && !user.getUserNotifications().contains(this)) {
            user.getUserNotifications().add(this);
        }
    }

}