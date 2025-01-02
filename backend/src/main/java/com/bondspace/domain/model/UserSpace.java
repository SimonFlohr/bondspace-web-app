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

    // GETTERS & SETTERS


}