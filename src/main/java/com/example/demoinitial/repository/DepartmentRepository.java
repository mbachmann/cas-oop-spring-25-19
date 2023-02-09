package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select d from Department d left join fetch d.employees where d.id =:id")
    Optional<Department> findByIdWithEagerRelationships(@Param("id") Long id);

}
