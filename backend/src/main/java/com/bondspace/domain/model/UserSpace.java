package com.bondspace.domain.model;

import com.bondspace.domain.model.enums.SpaceUserRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_space")
public class UserSpace {

    // ATTRIBUTES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private SpaceUserRole userRole = SpaceUserRole.NONE;

    @Column(name = "user_joined_at", updatable = false)
    private LocalDateTime userJoinedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "space_id", nullable = false)
    private Space space;

    // CONSTRUCTORS
    public UserSpace() {};

    public UserSpace(SpaceUserRole userRole, User user, Space space) {
        this.userRole = userRole;
        this.user = user;
        this.space = space;
    }

    // GETTERS & SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpaceUserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(SpaceUserRole userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getUserJoinedAt() {
        return userJoinedAt;
    }

    public void setUserJoinedAt(LocalDateTime userJoinedAt) {
        this.userJoinedAt = userJoinedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    // METHODS
    

}