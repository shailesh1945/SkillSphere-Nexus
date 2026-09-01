package com.skillsphere.nexus.skill_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AssessmentResponse {

    private UUID assessmentId;

    private String assessmentName;

    private LocalDate assessmentDate;

    private UUID employeeId;

    private UUID skillId;

    private Double score;

    private Boolean passed;

    private String result;

    private Boolean verified;
}