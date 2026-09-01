package com.skillsphere.nexus.career_service.service.impl;

import com.skillsphere.nexus.career_service.dto.request.CareerPlanRequest;
import com.skillsphere.nexus.career_service.dto.response.CareerPlanResponse;
import com.skillsphere.nexus.career_service.model.CareerPlan;
import com.skillsphere.nexus.career_service.repository.CareerPlanRepository;
import com.skillsphere.nexus.career_service.service.CareerPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CareerPlanServiceImpl
        implements CareerPlanService {

    private final CareerPlanRepository careerPlanRepository;


    @Override
    public CareerPlanResponse createCareerPlan(
            CareerPlanRequest request) {

        int promotionScore =
                calculatePromotionScore(request);

        CareerPlan careerPlan = CareerPlan.builder()
                .employeeId(request.getEmployeeId())
                .employeeName(request.getEmployeeName())
                .currentRole(request.getCurrentRole())
                .targetRole(request.getTargetRole())
                .progress(request.getProgress())
                .mentor(request.getMentor())
                .skillGaps(request.getSkillGaps())
                .trainingPlan(request.getTrainingPlan())
                .promotionScore(promotionScore)
                .promotionEligible(promotionScore >= 80)
                .status(CareerPlan.PlanStatus.ACTIVE)
                .build();

        CareerPlan savedCareerPlan =
                careerPlanRepository.save(careerPlan);

        return mapToResponse(savedCareerPlan);
    }


    @Override
    public List<CareerPlanResponse> getAllCareerPlans() {

        return careerPlanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public CareerPlanResponse getCareerPlanById(
            UUID planId) {

        CareerPlan careerPlan =
                careerPlanRepository.findById(planId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Career plan not found with id: "
                                                + planId));

        return mapToResponse(careerPlan);
    }


    @Override
    public List<CareerPlanResponse> getCareerPlansByEmployee(
            UUID employeeId) {

        return careerPlanRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public CareerPlanResponse updateCareerPlan(
            UUID planId,
            CareerPlanRequest request) {

        CareerPlan careerPlan =
                careerPlanRepository.findById(planId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Career plan not found with id: "
                                                + planId));

        careerPlan.setCurrentRole(
                request.getCurrentRole());

        careerPlan.setTargetRole(
                request.getTargetRole());

        careerPlan.setProgress(
                request.getProgress());

        careerPlan.setMentor(
                request.getMentor());

        careerPlan.setSkillGaps(
                request.getSkillGaps());

        careerPlan.setTrainingPlan(
                request.getTrainingPlan());

        int promotionScore =
                calculatePromotionScore(request);

        careerPlan.setPromotionScore(
                promotionScore);

        careerPlan.setPromotionEligible(
                promotionScore >= 80);

        CareerPlan updatedCareerPlan =
                careerPlanRepository.save(careerPlan);

        return mapToResponse(updatedCareerPlan);
    }


    @Override
    public void deleteCareerPlan(UUID planId) {

        if (!careerPlanRepository.existsById(planId)) {

            throw new RuntimeException(
                    "Career plan not found with id: "
                            + planId);
        }

        careerPlanRepository.deleteById(planId);
    }


    // -----------------------------
    // Promotion Score Calculation
    // -----------------------------

    private int calculatePromotionScore(
            CareerPlanRequest request) {

        int score = 0;

        if (request.getProgress() != null) {

            score += (int)
                    (request.getProgress() * 0.6);
        }

        if (request.getSkillGaps() == null
                || request.getSkillGaps().isBlank()) {

            score += 20;
        }

        if (request.getTrainingPlan() != null
                && !request.getTrainingPlan().isBlank()) {

            score += 20;
        }

        return Math.min(score, 100);
    }


    // -----------------------------
    // Response Mapping
    // -----------------------------

    private CareerPlanResponse mapToResponse(
            CareerPlan careerPlan) {

        return CareerPlanResponse.builder()

                .planId(
                        careerPlan.getPlanId())

                .employeeId(
                        careerPlan.getEmployeeId())

                .employeeName(
                        careerPlan.getEmployeeName())

                .currentRole(
                        careerPlan.getCurrentRole())

                .targetRole(
                        careerPlan.getTargetRole())

                .progress(
                        careerPlan.getProgress())

                .mentor(
                        careerPlan.getMentor())

                .skillGaps(
                        careerPlan.getSkillGaps())

                .trainingPlan(
                        careerPlan.getTrainingPlan())

                .promotionScore(
                        careerPlan.getPromotionScore())

                .promotionEligible(
                        careerPlan.getPromotionEligible())

                .status(
                        careerPlan.getStatus().name())

                .build();
    }
}