package com.skillsphere.nexus.learning_service.repository;

import com.skillsphere.nexus.learning_service.model.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LearningPathRepository
        extends JpaRepository<LearningPath, UUID> {
}