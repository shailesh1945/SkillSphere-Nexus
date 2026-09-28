package com.skillsphere.nexus.skill_service.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssessmentQuestionRequest {

    @NotBlank
    private String questionText;

    @NotBlank
    private String optionA;

    @NotBlank
    private String optionB;

    @NotBlank
    private String optionC;

    @NotBlank
    private String optionD;

    @NotBlank
    private String correctAnswer;

    @NotNull
    private Integer marks;
}