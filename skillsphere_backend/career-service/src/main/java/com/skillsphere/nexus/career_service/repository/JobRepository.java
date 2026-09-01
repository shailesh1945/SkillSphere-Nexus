package com.skillsphere.nexus.career_service.repository;

import com.skillsphere.nexus.career_service.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobRepository
        extends JpaRepository<Job, UUID> {

    List<Job> findByActiveTrue();

    List<Job> findByDepartmentIgnoreCase(String department);
}