package com.example.demoinitial.repository;


import com.example.demoinitial.domain.User;
import com.example.demoinitial.domain.enums.ERole;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void getAdmin() {
        Optional<User> user = userRepository.findByEmail("admin@example.com");
        user.ifPresent(u -> {
            assertEquals(u.getEmail(), "admin@example.com");
            assertTrue(u.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN)));
        });

    }

    @Test
    public void getUser() {
        Optional<User>  user = userRepository.findByEmail("user@example.com");

        user.ifPresent(u -> {
            assertEquals(u.getEmail(), "user@example.com");
            assertTrue(u.getRoles().stream().anyMatch(role -> role.getName().equals(ERole.ROLE_USER)));
        });

    }
}
