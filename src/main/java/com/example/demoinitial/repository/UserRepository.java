package com.example.demoinitial.repository;


import com.example.demoinitial.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @EntityGraph(value = "User.Roles")
    Optional<User> findById(Long id);

    Optional<User> findByUsernameOrEmail(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email);
    Optional<User> findByUsername(String username);

    @EntityGraph(value = "User.Roles")
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);


}
