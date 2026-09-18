package com.skillsphere.nexus.career_service.service.impl;

import com.skillsphere.nexus.career_service.dto.response.AnalyticsResponse;
import com.skillsphere.nexus.career_service.model.CareerPlan;
import com.skillsphere.nexus.career_service.repository.CareerPlanRepository;
import com.skillsphere.nexus.career_service.repository.JobRepository;
import com.skillsphere.nexus.career_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl
        implements AnalyticsService {

    private final CareerPlanRepository careerPlanRepository;

    private final JobRepository jobRepository;


    @Override
    public AnalyticsResponse getAnalytics() {

        List<CareerPlan> plans =
                careerPlanRepository.findAll();


        double averageProgress =
                plans.stream()

                        .filter(plan ->
                                plan.getProgress() != null)

                        .mapToInt(
                                CareerPlan::getProgress)

                        .average()

                        .orElse(0.0);


        return AnalyticsResponse.builder()

                .totalCareerPlans(
                        plans.size())

                .activeCareerPlans(
                        careerPlanRepository
                                .countByStatus(
                                        CareerPlan.PlanStatus.ACTIVE))

                .completedPlans(
                        careerPlanRepository
                                .countByStatus(
                                        CareerPlan.PlanStatus.COMPLETED))

                .promotionEligible(
                        careerPlanRepository
                                .countByPromotionEligibleTrue())

                .averageProgress(
                        averageProgress)

                .skillCoverage(
                        calculateSkillCoverage(plans))

                .activeJobs(
                        jobRepository
                                .findByActiveTrue()
                                .size())

                .build();
    }


    private double calculateSkillCoverage(
            List<CareerPlan> plans) {

        if (plans.isEmpty()) {
            return 0.0;
        }


        long withoutGap =
                plans.stream()

                        .filter(plan ->
                                plan.getSkillGaps() == null
                                        || plan.getSkillGaps()
                                        .isBlank())

                        .count();


        return (withoutGap * 100.0)
                / plans.size();
    }
}