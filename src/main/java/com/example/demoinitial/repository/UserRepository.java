package com.example.demoinitial.repository;

import java.util.List;

import com.example.demoinitial.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);
    Optional<User> findByEmail(String name);


}
