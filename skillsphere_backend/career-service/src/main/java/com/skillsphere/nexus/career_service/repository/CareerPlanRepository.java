package com.skillsphere.nexus.career_service.repository;

import com.skillsphere.nexus.career_service.model.CareerPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CareerPlanRepository
        extends JpaRepository<CareerPlan, UUID> {

    List<CareerPlan> findByEmployeeId(UUID employeeId);

    long countByStatus(CareerPlan.PlanStatus status);

    long countByPromotionEligibleTrue();
}