package com.bondspace.repository;

import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.User;
import com.bondspace.domain.model.UserSpace;
import com.bondspace.domain.model.enums.SpaceUserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @BeforeEach
    void clearDatabase() {
        userRepository.deleteAll();
        spaceRepository.deleteAll();
        userSpaceRepository.deleteAll();
    }

    @Test
    @Transactional
    void testSaveAndFindUser() {

        // Preparation
        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);
        UserSpace savedUserSpace = userSpaceRepository.save(userSpace);

        // Interaction
        User foundUser = userRepository.findById(savedUser.getId()).get();

        user.getUserSpaces().add(userSpace);
        space.getUserSpaces().add(userSpace);

        // Assertions
        assertThat(savedUser).isNotNull();
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getFirstName()).isEqualTo("Simon");
        assertThat(foundUser.getEmailAddress()).isEqualTo("simonflohr95@gmail.com");
        assertThat(foundUser.getPassword()).isEqualTo("password123");
        assertThat(foundUser.getUserSpaces()).hasSize(1);
        assertThat(foundUser.getUserSpaces().get(0).getUserRole()).isEqualTo(SpaceUserRole.ADMIN);

    }

    @Test
    void testDeleteUser() {

        // Preparation
        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        // Interaction
        userRepository.deleteById(savedUser.getId());

        // Assertions
        assertThat(userRepository.findById(savedUser.getId())).isEmpty();

    }

}
