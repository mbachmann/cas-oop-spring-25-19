
package com.example.demoinitial.config;

import com.example.demoinitial.domain.Role;
import com.example.demoinitial.domain.User;
import com.example.demoinitial.domain.enums.ERole;
import com.example.demoinitial.repository.RoleRepository;
import com.example.demoinitial.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }

        Role role = createRoleIfNotFound(ERole.ROLE_ADMIN);
        role = createRoleIfNotFound(ERole.ROLE_USER);
        role = createRoleIfNotFound(ERole.ROLE_MODERATOR);

        Role adminRole = null;
        try {
            adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() ->
                new Exception("adminRole not found")
            );

            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() ->
                new Exception("userRole not found")
            );
            Role moderatorRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() ->
                new Exception("moderatorRole not found")
            );

            User user = createUserIfNotFound("admin", "admin@example.com",
                "admin", new HashSet<>(Arrays.asList(adminRole, userRole, moderatorRole)));

            user = createUserIfNotFound("user", "user@example.com",
                "user", new HashSet<>(Collections.singletonList(userRole)));

            user = createUserIfNotFound("moderator", "moderator@example.com",
                "moderator", new HashSet<>(Arrays.asList(userRole, moderatorRole)));

            alreadySetup = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    Role createRoleIfNotFound(ERole roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isPresent()) {
            return role.get();
        } else {
            Role newRole = new Role(roleName);
            return roleRepository.save(newRole);
        }
    }

    @Transactional
    User createUserIfNotFound(String userName, String eMail, String password, Set<Role> roleNames) {

        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()) {
            return user.get();
        } else {
            User newUser = new User(userName, eMail, passwordEncoder.encode(password), roleNames);
            return userRepository.save(newUser);
        }
    }
}
