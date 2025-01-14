package com.bondspace.repository;

import com.bondspace.domain.model.Memory;
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
public class MemoryRepositoryTest {

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @BeforeEach
    public void clearDatabase() {
        memoryRepository.deleteAll();
        userRepository.deleteAll();
        spaceRepository.deleteAll();
        userSpaceRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveAndFindMemory() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);
        UserSpace savedUserSpace = userSpaceRepository.save(userSpace);

        space.getUserSpaces().add(userSpace);
        user.getUserSpaces().add(userSpace);

        Memory memory = new Memory("TEXT", "Iowa Trip", userSpace.getUser(), userSpace.getSpace());
        Memory savedMemory = memoryRepository.save(memory);

        // Interaction
        Memory foundMemory = memoryRepository.findById(savedMemory.getId()).get();

        // Assertions
        assertThat(savedMemory).isNotNull();
        assertThat(foundMemory).isNotNull();
        assertThat(foundMemory.getSpace()).isEqualTo(space);
        assertThat(foundMemory.getUploadedBy()).isEqualTo(user);
        assertThat(foundMemory.getType()).isEqualTo("TEXT");
        assertThat(foundMemory.getName()).isEqualTo("Iowa Trip");

    }

    @Test
    public void testDeleteMemory() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        UserSpace userSpace = new UserSpace(SpaceUserRole.ADMIN, user, space);
        UserSpace savedUserSpace = userSpaceRepository.save(userSpace);

        space.getUserSpaces().add(userSpace);
        user.getUserSpaces().add(userSpace);

        Memory memory = new Memory("TEXT", "Iowa Trip", userSpace.getUser(), userSpace.getSpace());
        Memory savedMemory = memoryRepository.save(memory);

        // Interaction
        memoryRepository.deleteById(savedMemory.getId());

        // Assertions
        assertThat(memoryRepository.findById(savedMemory.getId())).isEmpty();

    }

}
