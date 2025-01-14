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
public class SpaceRepositoryTest {

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @BeforeEach
    public void clearDatabase() {
        userRepository.deleteAll();
        userSpaceRepository.deleteAll();
        spaceRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveAndFindSpace() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);
        UserSpace savedUserSpace = userSpaceRepository.save(userSpace);

        space.getUserSpaces().add(userSpace);
        user.getUserSpaces().add(userSpace);

        // Interaction
        Space foundSpace = spaceRepository.findById(savedSpace.getId()).get();

        // Assertions
        assertThat(savedSpace).isNotNull();
        assertThat(foundSpace).isNotNull();
        assertThat(foundSpace.getSpaceName()).isEqualTo("VanLoon Family Space");
        assertThat(foundSpace.getSpaceDescription()).isEqualTo("A shared space for the VanLoon family.");
        assertThat(foundSpace.getUserSpaces()).hasSize(1);
        assertThat(foundSpace.getUserSpaces().get(0).getUserRole()).isEqualTo(SpaceUserRole.ADMIN);

    }

    @Test
    public void testDeleteSpace() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");

        // Interaction
        spaceRepository.deleteById(space.getId());

        // Assertions
        assertThat(spaceRepository.findById(space.getId())).isEmpty();

    }

}
