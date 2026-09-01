package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.CertificationAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CertificationAuditRepository
        extends JpaRepository<CertificationAudit, UUID> {

    List<CertificationAudit>
    findByCertificationIdOrderByPerformedAtDesc(
            UUID certificationId);
}
