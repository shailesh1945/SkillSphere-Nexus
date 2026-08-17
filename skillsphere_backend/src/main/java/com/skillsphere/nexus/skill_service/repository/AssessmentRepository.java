package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
    List<Assessment> findByEmployeeEmployeeId(UUID employeeId);

    List<Assessment> findBySkillSkillId(UUID skillId);

    List<Assessment> findByResultIgnoreCase(String result);
}
