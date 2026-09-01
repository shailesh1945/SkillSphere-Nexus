package com.skillsphere.nexus.learning_service.service.impl;

import com.skillsphere.nexus.learning_service.dto.response.EnrollmentResponse;
import com.skillsphere.nexus.learning_service.model.Enrollment;
import com.skillsphere.nexus.learning_service.repository.EnrollmentRepository;
import com.skillsphere.nexus.learning_service.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl
        implements ProgressService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public EnrollmentResponse updateProgress(
            UUID enrollmentId,
            Integer progress) {

        if (progress < 0 || progress > 100) {

            throw new IllegalArgumentException(
                    "Progress must be between 0 and 100");
        }

        Enrollment enrollment =
                getEnrollment(enrollmentId);

        enrollment.setProgress(progress);

        if (progress == 100) {

            enrollment.setCompleted(true);
            enrollment.setCompletedAt(
                    LocalDateTime.now());
        }

        return mapToResponse(
                enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse submitAssessment(
            UUID enrollmentId,
            Float score) {

        if (score < 0 || score > 100) {

            throw new IllegalArgumentException(
                    "Score must be between 0 and 100");
        }

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Enrollment not found"));

        enrollment.setScore(score);

        return mapToResponse(
                enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse completeCourse(
            UUID enrollmentId) {

        Enrollment enrollment =
                getEnrollment(enrollmentId);

        enrollment.setProgress(100);
        enrollment.setCompleted(true);
        enrollment.setCompletedAt(
                LocalDateTime.now());

        return mapToResponse(
                enrollmentRepository.save(enrollment));
    }

    private Enrollment getEnrollment(
            UUID enrollmentId) {

        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Enrollment not found"));
    }

    private EnrollmentResponse mapToResponse(
            Enrollment enrollment) {

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .empId(enrollment.getEmpId())
                .courseId(
                        enrollment.getCourse().getCourseId())
                .courseTitle(
                        enrollment.getCourse().getTitle())
                .enrolledAt(enrollment.getEnrolledAt())
                .progress(enrollment.getProgress())
                .completed(enrollment.getCompleted())
                .score(enrollment.getScore())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }
}