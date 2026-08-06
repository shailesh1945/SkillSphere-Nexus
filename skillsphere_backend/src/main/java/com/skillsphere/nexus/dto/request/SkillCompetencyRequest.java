package com.skillsphere.nexus.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SkillCompetencyRequest {

    private UUID employeeId;

    private UUID skillId;

    private Integer proficiencyLevel;

    private Integer yearsOfExperience;
}