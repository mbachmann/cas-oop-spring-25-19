package com.example.demoinitial.repository;

import com.example.demoinitial.domain.Address;
import com.example.demoinitial.domain.Department;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select d from Department d left join fetch d.employees where d.id =:id")
    Optional<Department> findByIdWithEagerRelationships(@Param("id") Long id);


}
