package com.skillsphere.nexus.learning_service.controller;

import com.skillsphere.nexus.learning_service.dto.request.EnrollmentRequest;
import com.skillsphere.nexus.learning_service.dto.response.EnrollmentResponse;
import com.skillsphere.nexus.learning_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/learning/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @PostMapping
    public EnrollmentResponse enroll(
            @RequestBody EnrollmentRequest request) {

        return enrollmentService.enroll(request);
    }


    @GetMapping("/{enrollmentId}")
    public EnrollmentResponse getEnrollmentById(
            @PathVariable UUID enrollmentId) {

        return enrollmentService.getEnrollmentById(enrollmentId);
    }


    @GetMapping("/employee/{empId}")
    public List<EnrollmentResponse> getEmployeeEnrollments(
            @PathVariable UUID empId) {

        return enrollmentService.getEmployeeEnrollments(empId);
    }


    @GetMapping("/course/{courseId}")
    public List<EnrollmentResponse> getCourseEnrollments(
            @PathVariable UUID courseId) {

        return enrollmentService.getCourseEnrollments(courseId);
    }


    @DeleteMapping("/{enrollmentId}")
    public String deleteEnrollment(
            @PathVariable UUID enrollmentId) {

        enrollmentService.deleteEnrollment(enrollmentId);

        return "Enrollment deleted successfully";
    }
}