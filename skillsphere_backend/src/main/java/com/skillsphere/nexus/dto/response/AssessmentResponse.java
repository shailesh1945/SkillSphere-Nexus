package com.skillsphere.nexus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AssessmentResponse {

    private UUID assessmentId;

    private String assessmentName;

    private String employeeName;

    private String skillName;

    private Double score;

    private Double maxScore;

    private Boolean verified;

    private String status;
}