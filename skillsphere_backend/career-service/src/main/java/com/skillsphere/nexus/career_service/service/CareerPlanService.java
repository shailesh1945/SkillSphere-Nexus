package com.skillsphere.nexus.career_service.service;

import com.skillsphere.nexus.career_service.dto.request.CareerPlanRequest;
import com.skillsphere.nexus.career_service.dto.response.CareerPlanResponse;

import java.util.List;
import java.util.UUID;

public interface CareerPlanService {

    CareerPlanResponse createCareerPlan(
            CareerPlanRequest request);

    List<CareerPlanResponse> getAllCareerPlans();

    CareerPlanResponse getCareerPlanById(
            UUID planId);

    List<CareerPlanResponse> getCareerPlansByEmployee(
            UUID employeeId);

    CareerPlanResponse updateCareerPlan(
            UUID planId,
            CareerPlanRequest request);

    void deleteCareerPlan(UUID planId);
}