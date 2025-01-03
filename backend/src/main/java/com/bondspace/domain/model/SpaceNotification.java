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

    // CONSTRUCTORS
    public SpaceNotification() {};

    public SpaceNotification(String spaceNotificationType, String message, Space space) {
        this.spaceNotificationType = spaceNotificationType;
        this.message = message;
        this.space = space;
    }

    // GETTERS & SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSpaceNotificationType() {
        return spaceNotificationType;
    }

    public void setSpaceNotificationType(String spaceNotificationType) {
        this.spaceNotificationType = spaceNotificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    // METHODS
    public String buildNotification() {
        return "";
    }

    public boolean addToStream() {
        return false;
    }

}
