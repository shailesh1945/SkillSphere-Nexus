package com.skillsphere.nexus.learning_service.service;

import com.skillsphere.nexus.learning_service.dto.response.EnrollmentResponse;

import java.util.UUID;

public interface ProgressService {

    EnrollmentResponse updateProgress(
            UUID enrollmentId,
            Integer progress);

    EnrollmentResponse submitAssessment(
            UUID enrollmentId,
            Float score);

    EnrollmentResponse completeCourse(
            UUID enrollmentId);
}