package com.skillsphere.nexus.repository;

import com.skillsphere.nexus.model.SkillCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillCompetencyRepository extends JpaRepository<SkillCompetency, UUID> {

    List<SkillCompetency> findByEmployeeEmployeeId(UUID employeeId);


    List<SkillCompetency> findBySkillSkillId(UUID skillId);

    List<SkillCompetency> findByProficiencyLevel(Integer proficiencyLevel);

    boolean existsByEmployeeEmployeeIdAndSkillSkillId(UUID employeeId,
                                                      UUID skillId);


}
