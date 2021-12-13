package com.example.demoinitial.config;

import com.example.demoinitial.domain.Role;
import com.example.demoinitial.domain.User;
import com.example.demoinitial.repository.RoleRepository;
import com.example.demoinitial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

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

        if (alreadySetup)
            return;

        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");

        Role adminRole = roleRepository.findByRole("ROLE_ADMIN");
        Role userRole = roleRepository.findByRole("ROLE_USER");

        User user = new User("admin", "admin@example.com");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRoles(new HashSet<Role>(Arrays.asList(adminRole, userRole)));
        userRepository.save(user);

        user = new User("user", "admin@admin.ch");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setRoles(new HashSet<Role>(Arrays.asList(adminRole, userRole)));
        userRepository.save(user);

        user = new User("user", "user@example.com");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);

        alreadySetup = true;
    }


    @Transactional
    Role createRoleIfNotFound(String roleName) {

        Role role = roleRepository.findByRole(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }
        return role;
    }
}
