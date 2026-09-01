package com.skillsphere.nexus.career_service.controller;

import com.skillsphere.nexus.career_service.dto.request.CareerPlanRequest;
import com.skillsphere.nexus.career_service.dto.response.CareerPlanResponse;
import com.skillsphere.nexus.career_service.service.CareerPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/career/plans")
@RequiredArgsConstructor
public class CareerPlanController {

    private final CareerPlanService careerPlanService;


    @PostMapping
    public CareerPlanResponse createCareerPlan(
            @RequestBody CareerPlanRequest request) {

        return careerPlanService
                .createCareerPlan(request);
    }


    @GetMapping
    public List<CareerPlanResponse> getAllCareerPlans() {

        return careerPlanService
                .getAllCareerPlans();
    }


    @GetMapping("/{planId}")
    public CareerPlanResponse getCareerPlanById(
            @PathVariable UUID planId) {

        return careerPlanService
                .getCareerPlanById(planId);
    }


    @GetMapping("/employee/{employeeId}")
    public List<CareerPlanResponse>
    getCareerPlansByEmployee(
            @PathVariable UUID employeeId) {

        return careerPlanService
                .getCareerPlansByEmployee(employeeId);
    }


    @PutMapping("/{planId}")
    public CareerPlanResponse updateCareerPlan(
            @PathVariable UUID planId,
            @RequestBody CareerPlanRequest request) {

        return careerPlanService
                .updateCareerPlan(
                        planId,
                        request);
    }


    @DeleteMapping("/{planId}")
    public void deleteCareerPlan(
            @PathVariable UUID planId) {

        careerPlanService
                .deleteCareerPlan(planId);
    }
}