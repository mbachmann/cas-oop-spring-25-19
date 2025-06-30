package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Role;
import com.example.demoinitial.domain.enums.ERole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

    @Override
    void delete(Role role);

}
