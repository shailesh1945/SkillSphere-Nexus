package com.skillsphere.nexus.repository;

import com.skillsphere.nexus.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepository extends JpaRepository<Skill, UUID> {

    Optional<Skill> findBySkillName(String skillName);

    boolean existsBySkillName(String skillName);

    List<Skill> findByCategoryIgnoreCase(String category);

    List<Skill> findBySkillNameContainingIgnoreCase(String keyword);

    long count();
}