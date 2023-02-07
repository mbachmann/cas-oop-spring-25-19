package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select e from Employee e left join fetch e.address left join fetch e.phones left join fetch e.projects  left join fetch e.department left join fetch e.chef  left join fetch e.employees where e.id =:id")
    Optional<Employee> findByIdWithEagerRelationships(@Param("id") Long id);

    @Query("select e from Employee e left join fetch e.address left join fetch e.phones left join fetch e.projects  left join fetch e.department left join fetch e.chef  left join fetch e.employees")
    List<Employee> findAllWithEagerRelationships();

    @Query("select e from Employee e left join fetch e.address left join fetch e.phones left join fetch e.projects  left join fetch e.department left join fetch e.chef  left join fetch e.employees where e.name =:name")
    Optional<Employee> findByNameWithEagerRelationships(@Param("name") String name);
}
