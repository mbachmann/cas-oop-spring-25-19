package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("select p from Person p where p.lastName = :lastName")
    List<Person> findQueryByLastName(String lastName);
}
