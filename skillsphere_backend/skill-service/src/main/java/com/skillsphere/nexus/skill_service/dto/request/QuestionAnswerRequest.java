package com.skillsphere.nexus.skill_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class QuestionAnswerRequest {

    @NotNull
    private UUID questionId;

    @NotBlank
    private String answer;
}
