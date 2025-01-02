package com.bondspace.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "space_notifications")
public class SpaceNotification implements Notification {

    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "space_notification_type", nullable = false)
    private String spaceNotificationType;

    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    // GETTERS & SETTERS


    // METHODS
    public String buildNotification() {
        return "";
    }

    public boolean addToStream() {
        return false;
    }

}
