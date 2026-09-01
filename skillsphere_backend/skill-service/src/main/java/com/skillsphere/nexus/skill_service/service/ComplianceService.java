package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.response.ComplianceResponse;

import java.util.UUID;

public interface ComplianceService {

    ComplianceResponse getCompliance(
            UUID employeeId);
}
