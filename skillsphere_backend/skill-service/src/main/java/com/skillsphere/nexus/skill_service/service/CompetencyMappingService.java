package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.response.CompetencyGapResponse;

import java.util.List;
import java.util.UUID;

public interface CompetencyMappingService {

    List<CompetencyGapResponse> getCompetencyGap(
            UUID employeeId,
            String roleTitle);

}