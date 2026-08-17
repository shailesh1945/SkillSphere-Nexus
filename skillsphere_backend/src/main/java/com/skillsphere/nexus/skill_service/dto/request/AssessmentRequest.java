package com.skillsphere.nexus.skill_service.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AssessmentRequest {

    private UUID employeeId;

    private UUID skillId;

    private String assessmentName;

    private Double score;

    private Double maxScore;

    private Boolean verified;

    private LocalDate assessmentDate;
}