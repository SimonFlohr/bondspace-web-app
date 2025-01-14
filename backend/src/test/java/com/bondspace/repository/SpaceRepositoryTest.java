package com.bondspace.repository;

import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.User;
import com.bondspace.domain.model.UserSpace;
import com.bondspace.domain.model.enums.SpaceUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpaceRepositoryTest {

    @Autowired
    private SpaceRepository spaceRepository;

    @BeforeEach
    public void clearDatabase() { spaceRepository.deleteAll(); }

    @Test
    public void testSaveAndFindSpace() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");

        User user = new User("simonflohr95@gmail.com", "password123", "Simon");

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);

        // Interaction
        Space savedSpace = spaceRepository.save(space);
        Space foundSpace = spaceRepository.findById(savedSpace.getId()).get();

        // Assertions
        assertThat(savedSpace).isNotNull();
        assertThat(foundSpace).isNotNull();
        assertThat(foundSpace.getSpaceName()).isEqualTo("VanLoon Family Space");
        assertThat(foundSpace.getSpaceDescription()).isEqualTo("A shared space for the VanLoon family.");

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
