package com.skillsphere.nexus.controller;

import com.skillsphere.nexus.dto.request.AssessmentRequest;
import com.skillsphere.nexus.dto.response.AssessmentResponse;
import com.skillsphere.nexus.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentResponse createAssessment(@RequestBody AssessmentRequest dto) {
        return assessmentService.addAssessment(dto);
    }

    @PutMapping("/{id}/verify")
    @PreAuthorize("hasRole('HR')")
    public AssessmentResponse verifyAssessment(@PathVariable UUID id){
        return assessmentService.verifyAssessment(id);
    }
}

