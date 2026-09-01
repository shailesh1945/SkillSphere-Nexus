package com.skillsphere.nexus.skill_service.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AssessmentRequest {

    @NotBlank
    private String assessmentName;

    private LocalDate assessmentDate;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double score;

    @NotNull
    private UUID employeeId;

    @NotNull
    private UUID skillId;

}