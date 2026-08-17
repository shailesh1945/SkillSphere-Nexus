package com.skillsphere.nexus.learning_service.repository;

import com.skillsphere.nexus.learning_service.model.LearningCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LearningCertificateRepository extends JpaRepository<LearningCertificate, UUID> {

}
