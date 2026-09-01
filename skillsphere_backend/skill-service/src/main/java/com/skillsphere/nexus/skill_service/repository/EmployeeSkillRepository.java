package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeSkillRepository
        extends JpaRepository<EmployeeSkill, UUID> {

    List<EmployeeSkill> findByEmployeeEmployeeId(UUID employeeId);

    List<EmployeeSkill> findBySkillSkillId(UUID skillId);

    Optional<EmployeeSkill> findByEmployeeEmployeeIdAndSkillSkillId(
            UUID employeeId,
            UUID skillId
    );

    boolean existsByEmployeeEmployeeIdAndSkillSkillId(
            UUID employeeId,
            UUID skillId
    );

    List<EmployeeSkill> findByProficiencyLessThan(Integer proficiency);

    List<EmployeeSkill> findByProficiencyGreaterThanEqual(
            Integer proficiency
    );
}
