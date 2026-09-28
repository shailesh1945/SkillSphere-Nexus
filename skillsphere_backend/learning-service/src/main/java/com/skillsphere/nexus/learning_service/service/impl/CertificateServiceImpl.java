package com.skillsphere.nexus.learning_service.service.impl;

import com.skillsphere.nexus.learning_service.model.Enrollment;
import com.skillsphere.nexus.learning_service.model.LearningCertificate;
import com.skillsphere.nexus.learning_service.repository.EnrollmentRepository;
import com.skillsphere.nexus.learning_service.repository.LearningCertificateRepository;
import com.skillsphere.nexus.learning_service.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final EnrollmentRepository enrollmentRepository;
    private final LearningCertificateRepository certificateRepository;

    public LearningCertificate generateCertificate(
            UUID enrollmentId) {

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Enrollment not found"));

        // 100% is the actual completion requirement
        if (enrollment.getProgress() == null ||
                enrollment.getProgress() < 100) {

            throw new RuntimeException(
                    "Course is not completed");
        }

        // Keep completion state synchronized
        if (!Boolean.TRUE.equals(
                enrollment.getCompleted())) {

            enrollment.setCompleted(true);

            enrollment.setCompletedAt(
                    enrollment.getCompletedAt() != null
                            ? enrollment.getCompletedAt()
                            : LocalDateTime.now());

            enrollment =
                    enrollmentRepository.saveAndFlush(
                            enrollment);
        }

        LearningCertificate certificate =
                LearningCertificate.builder()
                        .empId(enrollment.getEmpId())
                        .courseId(
                                enrollment.getCourse().getCourseId())
                        .courseName(
                                enrollment.getCourse().getTitle())
                        .score(enrollment.getScore())
                        .issuedDate(LocalDate.now())
                        .certificateNumber(
                                "SS-" + UUID.randomUUID())
                        .build();

        return certificateRepository.save(certificate);
    }
}
