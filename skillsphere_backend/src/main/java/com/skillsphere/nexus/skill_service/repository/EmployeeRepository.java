package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Employee> findByDepartmentIgnoreCase(String department);

    List<Employee> findByDesignationIgnoreCase(String designation);

    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName);

    List<Employee> findByActive(Boolean active);
    long count();
}
