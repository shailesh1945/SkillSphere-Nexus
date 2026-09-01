package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.CertificationRenewal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificationRenewalRepository
        extends JpaRepository<CertificationRenewal, UUID> {

        List<CertificationRenewal> findByCertificationCertificationId(UUID certificationId);
}
