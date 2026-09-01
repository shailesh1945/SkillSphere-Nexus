package com.skillsphere.nexus.learning_service.service;

import com.skillsphere.nexus.learning_service.dto.request.EnrollmentRequest;
import com.skillsphere.nexus.learning_service.dto.response.EnrollmentResponse;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {

    EnrollmentResponse enroll(EnrollmentRequest request);

    EnrollmentResponse getEnrollmentById(UUID enrollmentId);

    List<EnrollmentResponse> getEmployeeEnrollments(UUID empId);

    List<EnrollmentResponse> getCourseEnrollments(UUID courseId);


    void deleteEnrollment(UUID enrollmentId);
}