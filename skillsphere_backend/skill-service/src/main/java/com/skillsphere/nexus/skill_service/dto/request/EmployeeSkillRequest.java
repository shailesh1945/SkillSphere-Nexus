package com.skillsphere.nexus.skill_service.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeSkillRequest {

    @NotNull
    private UUID employeeId;

    @NotNull
    private UUID skillId;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer proficiency;
}
