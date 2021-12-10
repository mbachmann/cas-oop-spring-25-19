package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String name);

    @Override
    void delete(Role role);

}
