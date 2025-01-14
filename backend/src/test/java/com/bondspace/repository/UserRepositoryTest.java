package com.bondspace.repository;

import com.bondspace.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clearDatabase() { userRepository.deleteAll(); }

    @Test
    void testSaveAndFindUser() {

        // Preparation
        User user = new User("simonflohr95@gmail.com", "password123", "Simon");

        // Interaction
        User savedUser = userRepository.save(user);
        User foundUser = userRepository.findById(savedUser.getId()).get();

        // Assertions
        assertThat(savedUser).isNotNull();
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getFirstName()).isEqualTo("Simon");
        assertThat(foundUser.getEmailAddress()).isEqualTo("simonflohr95@gmail.com");
        assertThat(foundUser.getPassword()).isEqualTo("password123");

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
