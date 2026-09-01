package com.skillsphere.nexus.career_service.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {

    private long totalCareerPlans;

    private long activeCareerPlans;

    private long completedPlans;

    private long promotionEligible;

    private double averageProgress;

    private double skillCoverage;

    private long activeJobs;
}
