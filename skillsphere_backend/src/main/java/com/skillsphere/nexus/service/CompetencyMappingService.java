package com.skillsphere.nexus.service;

import com.skillsphere.nexus.dto.response.CompetencyGapResponse;

import java.util.List;
import java.util.UUID;

public interface CompetencyMappingService {

    List<CompetencyGapResponse> getCompetencyGap(
            UUID employeeId,
            String roleTitle);

}