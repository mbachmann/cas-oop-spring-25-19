package com.example.demoinitial.repository;


import com.example.demoinitial.domain.User;
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
        User user = userRepository.findByEmail("admin@example.com");
        assertEquals(user.getEmail(), "admin@example.com");
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getRole().equals("ROLE_ADMIN")));
    }

    @Test
    public void getUser() {
        User user = userRepository.findByEmail("user@example.com");
        assertEquals(user.getEmail(), "user@example.com");
        assertTrue(user.getRoles().stream().anyMatch(role -> role.getRole().equals("ROLE_USER")));
    }
}
