package com.skillsphere.nexus.skill_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class AssessmentRequest {

    @NotBlank
    private String assessmentName;

    private LocalDate assessmentDate;

    @NotNull
    private UUID employeeId;

    @NotNull
    private UUID skillId;

    @Valid
    private List<AssessmentQuestionRequest> questions;
}