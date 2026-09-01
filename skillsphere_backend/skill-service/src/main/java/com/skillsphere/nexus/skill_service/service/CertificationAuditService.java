package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.response.CertificationAuditResponse;

import java.util.List;
import java.util.UUID;

public interface CertificationAuditService {

    void log(
            UUID certificationId,
            UUID employeeId,
            String action,
            String performedBy);

    List<CertificationAuditResponse>
    getAudit(UUID certificationId);

}
