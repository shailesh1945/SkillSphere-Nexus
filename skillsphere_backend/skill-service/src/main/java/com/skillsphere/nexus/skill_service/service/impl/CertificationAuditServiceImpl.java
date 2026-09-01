package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.response.CertificationAuditResponse;
import com.skillsphere.nexus.skill_service.model.CertificationAudit;
import com.skillsphere.nexus.skill_service.repository.CertificationAuditRepository;
import com.skillsphere.nexus.skill_service.service.CertificationAuditService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificationAuditServiceImpl implements CertificationAuditService {

    private final CertificationAuditRepository repository;

    @Override
    public void log(
            UUID certificationId,
            UUID employeeId,
            String action,
            String performedBy) {

        CertificationAudit audit =
                CertificationAudit.builder()
                        .certificationId(certificationId)
                        .employeeId(employeeId)
                        .action(action)
                        .performedBy(performedBy)
                        .performedAt(
                                LocalDateTime.now())
                        .build();

        repository.save(audit);
    }

    @Override
    public List<CertificationAuditResponse>
    getAudit(UUID certificationId) {

        return repository
                .findByCertificationIdOrderByPerformedAtDesc(
                        certificationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private CertificationAuditResponse mapToResponse(
            CertificationAudit audit) {

        return CertificationAuditResponse.builder()
                .auditId(audit.getAuditId())
                .certificationId(
                        audit.getCertificationId())
                .employeeId(
                        audit.getEmployeeId())
                .action(audit.getAction())
                .performedBy(
                        audit.getPerformedBy())
                .performedAt(
                        audit.getPerformedAt())
                .build();
    }
}
