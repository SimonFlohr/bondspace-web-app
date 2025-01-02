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

    // GETTERS & SETTERS


    // METHODS
    public String buildNotification() {
        return "";
    }

    public boolean addToStream() {
        return false;
    }

}