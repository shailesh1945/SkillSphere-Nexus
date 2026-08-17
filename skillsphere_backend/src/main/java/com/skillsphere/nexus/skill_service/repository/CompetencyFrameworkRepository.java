package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.CompetencyFramework;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CompetencyFrameworkRepository
        extends JpaRepository<CompetencyFramework, UUID> {

    List<CompetencyFramework> findByRoleTitle(String roleTitle);

}