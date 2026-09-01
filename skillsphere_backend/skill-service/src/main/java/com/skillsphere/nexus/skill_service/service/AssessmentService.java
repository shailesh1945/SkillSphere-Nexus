package com.skillsphere.nexus.skill_service.service;

import com.skillsphere.nexus.skill_service.dto.request.AssessmentRequest;
import com.skillsphere.nexus.skill_service.dto.response.AssessmentResponse;

import java.util.List;
import java.util.UUID;

public interface AssessmentService {

    // CRUD

    AssessmentResponse addAssessment(AssessmentRequest request);

    AssessmentResponse updateAssessment(UUID assessmentId,
                                        AssessmentRequest request);

    AssessmentResponse getAssessmentById(UUID assessmentId);

    List<AssessmentResponse> getAllAssessments();

    void deleteAssessment(UUID assessmentId);


    List<AssessmentResponse> getAssessmentsByEmployee(UUID employeeId);

    List<AssessmentResponse> getAssessmentsBySkill(UUID skillId);

    Double calculateAverageScore(UUID employeeId);

    List<AssessmentResponse> getPassedAssessments();

    List<AssessmentResponse> getFailedAssessments();

    AssessmentResponse verifyAssessment(UUID assessmentId);
}