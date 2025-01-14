package com.bondspace.repository;

import com.bondspace.domain.model.User;
import com.bondspace.domain.model.UserNotification;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserNotificationRepositoryTest {

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clearDatabase() {
        userNotificationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testSaveAndFindUserNotification() {

        // Preparation
        User user = new User("simonflohr95@gmail.com", "password123", "Simon");
        User savedUser = userRepository.save(user);

        UserNotification userNotification = new UserNotification("TEXT", "Eline has posted in the space!", savedUser);
        UserNotification savedUserNotification = userNotificationRepository.save(userNotification);

        user.getUserNotifications().add(userNotification);

        // Interaction
        UserNotification foundUserNotification = userNotificationRepository.findById(savedUserNotification.getId()).get();

        // Assertions
        assertThat(savedUserNotification).isNotNull();
        assertThat(foundUserNotification).isNotNull();
        assertThat(foundUserNotification.getUser()).isEqualTo(user);
        assertThat(foundUserNotification.getUserNotificationType()).isEqualTo("TEXT");
        assertThat(foundUserNotification.getMessage()).isEqualTo("Eline has posted in the space!");
        assertThat(user.getUserNotifications()).isNotEmpty();
        assertThat(user.getUserNotifications()).hasSize(1);

    }

    @Test
    public void testDeleteUserNotification() {

        // Preparation
        User user = new User("simonflohr96@gmail.com", "password123", "Nico");
        User savedUser = userRepository.save(user);

        UserNotification userNotification = new UserNotification("TEXT", "Eline has posted in the space!", savedUser);
        UserNotification savedUserNotification = userNotificationRepository.save(userNotification);

        user.getUserNotifications().add(userNotification);

        // Interaction
        userNotificationRepository.deleteById(savedUserNotification.getId());

        // Assertions
        assertThat(userNotificationRepository.findById(savedUserNotification.getId())).isEmpty();

    }

}
