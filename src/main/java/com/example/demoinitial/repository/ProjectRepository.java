package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select distinct p from Project p left join fetch p.employees where p.name =:name")
    List<Project> findWithEagerProjectByName(@Param("name") String name);

    List< Project> findAllByType(String type, Sort sort);

    Page< Project> findAllByType(String type, Pageable pageable);
    List< Project> findAllByType(String type);

    Project findByTypeAndName(String type, String title);

    @Query("select p from Project p left join fetch p.employees where p.id =:id")
    Optional<Project> findByIdWithEagerRelationships(@Param("id") Long id);

}
