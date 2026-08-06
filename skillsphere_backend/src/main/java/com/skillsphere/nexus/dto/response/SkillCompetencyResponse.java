package com.skillsphere.nexus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SkillCompetencyResponse {

    private UUID competencyId;

    private String employeeName;

    private String skillName;

    private Integer proficiencyLevel;

    private Integer yearsOfExperience;

    private LocalDateTime lastUpdated;
}