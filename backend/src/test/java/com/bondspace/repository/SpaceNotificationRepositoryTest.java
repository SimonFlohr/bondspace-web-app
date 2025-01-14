package com.bondspace.repository;

import com.bondspace.domain.model.Space;
import com.bondspace.domain.model.SpaceNotification;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpaceNotificationRepositoryTest {

    @Autowired
    private SpaceNotificationRepository spaceNotificationRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @BeforeEach
    public void clearDatabase() {
        spaceNotificationRepository.deleteAll();
        spaceRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveAndFindSpaceNotification() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        SpaceNotification spaceNotification = new SpaceNotification("TEXT", "Eline has posted in the space.", space);
        SpaceNotification savedSpaceNotification = spaceNotificationRepository.save(spaceNotification);

        space.getSpaceNotifications().add(savedSpaceNotification);

        // Interaction
        SpaceNotification foundSpaceNotification = spaceNotificationRepository.findById(savedSpaceNotification.getId()).get();

        // Assertions
        assertThat(savedSpaceNotification).isNotNull();
        assertThat(foundSpaceNotification).isNotNull();
        assertThat(foundSpaceNotification.getSpace()).isEqualTo(space);
        assertThat(foundSpaceNotification.getSpaceNotificationType()).isEqualTo("TEXT");
        assertThat(foundSpaceNotification.getMessage()).isEqualTo("Eline has posted in the space.");
        assertThat(space.getSpaceNotifications()).isNotEmpty();
        assertThat(space.getSpaceNotifications()).hasSize(1);

    }

    @Test
    public void testDeleteSpaceNotification() {

        // Preparation
        Space space = new Space("VanLoon Family Space", "A shared space for the VanLoon family.");
        Space savedSpace = spaceRepository.save(space);

        SpaceNotification spaceNotification = new SpaceNotification("TEXT", "Eline has posted in the space.", space);
        SpaceNotification savedSpaceNotification = spaceNotificationRepository.save(spaceNotification);

        space.getSpaceNotifications().add(spaceNotification);

        // Interaction
        spaceNotificationRepository.deleteById(savedSpaceNotification.getId());

        // Assertions
        assertThat(spaceNotificationRepository.findById(savedSpaceNotification.getId())).isEmpty();

    }

}
