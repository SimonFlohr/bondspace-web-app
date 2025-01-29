package com.bondspace.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "spaces")
public class Space {

    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "space_name", nullable = false)
    private String spaceName;

    @Column(name = "description")
    private String spaceDescription;

    @Column(name = "space_picture")
    private String spacePicture;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSpace> userSpaces = new HashSet<>();  //

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SpaceNotification> spaceNotifications = new HashSet<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Memory> memories = new HashSet<>();

    // CONSTRUCTORS
    public Space() {};

    public Space(String spaceName, String spaceDescription) {
        this.spaceName = spaceName;
        this.spaceDescription = spaceDescription;
    }

    public Space(String spaceName, String spaceDescription, Set<UserSpace> userSpaces) {
        this.spaceName = spaceName;
        this.spaceDescription = spaceDescription;
        this.userSpaces = userSpaces;
    }

    // GETTERS & SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSpaceDescription() {
        return spaceDescription;
    }

    public void setSpaceDescription(String spaceDescription) {
        this.spaceDescription = spaceDescription;
    }

    public String getSpacePicture() {
        return spacePicture;
    }

    public void setSpacePicture(String spacePicture) {
        this.spacePicture = spacePicture;
    }

    public Set<UserSpace> getUserSpaces() {
        return userSpaces;
    }

    public void setUserSpaces(Set<UserSpace> userSpaces) {
        this.userSpaces = userSpaces;
    }

    public Set<SpaceNotification> getSpaceNotifications() {
        return spaceNotifications;
    }

    public void setSpaceNotifications(Set<SpaceNotification> spaceNotifications) {
        this.spaceNotifications = spaceNotifications;
    }

    public Set<Memory> getMemories() {
        return memories;
    }

    public void setMemories(Set<Memory> memories) {
        this.memories = memories;
    }

    // METHODS


}