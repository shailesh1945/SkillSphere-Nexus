package com.skillsphere.nexus.skill_service.repository;

import com.skillsphere.nexus.skill_service.model.Certification;
import com.skillsphere.nexus.skill_service.model.Certification.Status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, UUID> {
    List<Certification> findByEmployeeEmployeeId(UUID employeeId);

    List<Certification> findByStatusIgnoreCase(Status status);

    List<Certification> findByExpiryDateBefore(LocalDate date);

    List<Certification> findByExpiryDateBetween(LocalDate startDate,
                                                LocalDate endDate);
}
