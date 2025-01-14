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
public class UserSpaceRepositoryTest {

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @BeforeEach
    public void clearDatabase() {
        userSpaceRepository.deleteAll();
        userRepository.deleteAll();
        spaceRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveAndFindUserSpace() {

        // Preparation
        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);
        UserSpace savedUserSpace = userSpaceRepository.save(userSpace);

        user.getUserSpaces().add(savedUserSpace);
        space.getUserSpaces().add(savedUserSpace);

        // Interaction
        UserSpace foundUserSpace = userSpaceRepository.findById(savedUserSpace.getId()).get();

        // Assertions
        assertThat(savedUserSpace).isNotNull();
        assertThat(foundUserSpace).isNotNull();
        assertThat(foundUserSpace.getUser()).isEqualTo(user);
        assertThat(foundUserSpace.getSpace()).isEqualTo(space);
        assertThat(foundUserSpace.getUserRole()).isEqualTo(SpaceUserRole.ADMIN);

    }

    @Test
    public void testDeleteUserSpace() {

        // Preparation
        User user = new User("simonflohr96@gmail.com", "password123", "Eline");
        User savedUser = userRepository.save(user);

        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);
        UserSpace savedUserSpace = userSpaceRepository.save(userSpace);

        user.getUserSpaces().add(savedUserSpace);
        space.getUserSpaces().add(savedUserSpace);

        // Interaction
        userSpaceRepository.deleteById(savedUserSpace.getId());

        // Assertions
        assertThat(userSpaceRepository.findById(savedUserSpace.getId())).isEmpty();

    }

}
