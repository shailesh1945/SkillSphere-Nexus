package com.skillsphere.nexus.career_service.controller;

import com.skillsphere.nexus.career_service.dto.request.CareerPlanRequest;
import com.skillsphere.nexus.career_service.dto.response.CareerPlanResponse;
import com.skillsphere.nexus.career_service.service.CareerPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/career/plans")
@RequiredArgsConstructor
public class CareerPlanController {

    private final CareerPlanService careerPlanService;


    @PostMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public CareerPlanResponse createCareerPlan(
            @RequestBody CareerPlanRequest request) {

        return careerPlanService
                .createCareerPlan(request);
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CareerPlanResponse> getAllCareerPlans() {

        return careerPlanService
                .getAllCareerPlans();
    }


    @GetMapping("/{planId}")
    @PreAuthorize("isAuthenticated()")
    public CareerPlanResponse getCareerPlanById(
            @PathVariable UUID planId) {

        return careerPlanService
                .getCareerPlanById(planId);
    }


    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public List<CareerPlanResponse>
    getCareerPlansByEmployee(
            @PathVariable UUID employeeId) {

        return careerPlanService
                .getCareerPlansByEmployee(employeeId);
    }


    @PutMapping("/{planId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public CareerPlanResponse updateCareerPlan(
            @PathVariable UUID planId,
            @RequestBody CareerPlanRequest request) {

        return careerPlanService
                .updateCareerPlan(
                        planId,
                        request);
    }


    @DeleteMapping("/{planId}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public void deleteCareerPlan(
            @PathVariable UUID planId) {

        careerPlanService
                .deleteCareerPlan(planId);
    }
}