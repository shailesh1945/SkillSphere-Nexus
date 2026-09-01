package com.skillsphere.nexus.career_service.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerPlanRequest {

    private UUID employeeId;

    private String employeeName;

    private String currentRole;

    private String targetRole;

    private Integer progress;

    private String mentor;

    private String skillGaps;

    private String trainingPlan;
}