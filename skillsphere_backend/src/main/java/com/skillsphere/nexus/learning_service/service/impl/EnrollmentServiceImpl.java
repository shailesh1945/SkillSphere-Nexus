package com.skillsphere.nexus.learning_service.service.impl;

import com.skillsphere.nexus.learning_service.dto.request.EnrollmentRequest;
import com.skillsphere.nexus.learning_service.dto.response.EnrollmentResponse;
import com.skillsphere.nexus.learning_service.model.Course;
import com.skillsphere.nexus.learning_service.model.Enrollment;
import com.skillsphere.nexus.learning_service.repository.CourseRepository;
import com.skillsphere.nexus.learning_service.repository.EnrollmentRepository;
import com.skillsphere.nexus.learning_service.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;


    /*** Enroll Employee ***/
    @Override
    public EnrollmentResponse enroll(EnrollmentRequest request) {

        if (enrollmentRepository.existsByEmpIdAndCourseCourseId(
                request.getEmpId(),
                request.getCourseId())) {

            throw new RuntimeException(
                    "Employee is already enrolled in this course");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        if (!Boolean.TRUE.equals(course.getActive())) {
            throw new RuntimeException(
                    "Cannot enroll in an inactive course");
        }

        Enrollment enrollment = Enrollment.builder()
                .empId(request.getEmpId())
                .course(course)
                .enrolledAt(LocalDateTime.now())
                .progress(0)
                .completed(false)
                .score(0.0f)
                .completedAt(null)
                .build();

        Enrollment savedEnrollment =
                enrollmentRepository.save(enrollment);

        return mapToResponse(savedEnrollment);
    }


    @Override
    public EnrollmentResponse getEnrollmentById(UUID enrollmentId) {

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Enrollment not found with id: "
                                                + enrollmentId));

        return mapToResponse(enrollment);
    }


    @Override
    public List<EnrollmentResponse> getEmployeeEnrollments(UUID empId) {

        return enrollmentRepository.findByEmpId(empId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public List<EnrollmentResponse> getCourseEnrollments(UUID courseId) {

        courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new RuntimeException("Course not found"));

        return enrollmentRepository
                .findByCourseCourseId(courseId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


//    /** Update Progress **/
//    @Override
//    public EnrollmentResponse updateProgress(
//            UUID enrollmentId,
//            Integer progress,
//            Float score) {
//
//        Enrollment enrollment =
//                enrollmentRepository.findById(enrollmentId)
//                        .orElseThrow(() ->
//                                new RuntimeException(
//                                        "Enrollment not found"));
//
//        if (progress < 0 || progress > 100) {
//            throw new RuntimeException(
//                    "Progress must be between 0 and 100");
//        }
//
//        enrollment.setProgress(progress);
//        enrollment.setScore(score);
//
//        if (progress == 100) {
//
//            enrollment.setCompleted(true);
//            enrollment.setCompletedAt(LocalDateTime.now());
//
//        } else {
//
//            enrollment.setCompleted(false);
//            enrollment.setCompletedAt(null);
//        }
//
//        Enrollment updatedEnrollment =
//                enrollmentRepository.save(enrollment);
//
//        return mapToResponse(updatedEnrollment);
//    }


    @Override
    public void deleteEnrollment(UUID enrollmentId) {

        Enrollment enrollment =
                enrollmentRepository.findById(enrollmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Enrollment not found"));

        enrollmentRepository.delete(enrollment);
    }


    // Helper method

    private EnrollmentResponse mapToResponse(
            Enrollment enrollment) {

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .empId(enrollment.getEmpId())
                .courseTitle(enrollment.getCourse().getTitle())
                .courseId(enrollment.getCourse().getCourseId())
                .enrolledAt(enrollment.getEnrolledAt())
                .progress(enrollment.getProgress())
                .completed(enrollment.getCompleted())
                .score(enrollment.getScore())
                .completedAt(enrollment.getCompletedAt())
                .build();
    }
}