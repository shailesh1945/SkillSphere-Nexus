package com.skillsphere.nexus.career_service.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerPlanResponse {

    private UUID planId;

    private UUID employeeId;

    private String employeeName;

    private String currentRole;

    private String targetRole;

    private Integer progress;

    private String mentor;

    private String skillGaps;

    private String trainingPlan;

    private Integer promotionScore;

    private Boolean promotionEligible;

    private String status;
}