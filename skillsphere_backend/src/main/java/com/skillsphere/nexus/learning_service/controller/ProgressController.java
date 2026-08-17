package com.skillsphere.nexus.learning_service.controller;


import com.skillsphere.nexus.learning_service.dto.response.EnrollmentResponse;
import com.skillsphere.nexus.learning_service.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/learning/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @PutMapping("/{enrollmentId}")
    public EnrollmentResponse updateProgress(
            @PathVariable UUID enrollmentId,
            @RequestParam Integer progress ) {

        return progressService.updateProgress(
                enrollmentId,
                progress);
    }

    @PostMapping("/{enrollmentId}/assessment")
    public EnrollmentResponse submitAssessment(
            @PathVariable UUID enrollmentId,
            @RequestParam Float score) {

        return progressService.submitAssessment(
                enrollmentId,
                score);
    }

    @PostMapping("/{enrollmentId}/complete")
    public EnrollmentResponse completeCourse(
            @PathVariable UUID enrollmentId) {

        return progressService.completeCourse(
                enrollmentId);
    }
}