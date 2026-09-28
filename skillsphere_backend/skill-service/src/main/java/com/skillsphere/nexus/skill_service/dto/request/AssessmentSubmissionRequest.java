package com.skillsphere.nexus.skill_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AssessmentSubmissionRequest {

    @NotEmpty
    private List<QuestionAnswerRequest> answers;
}