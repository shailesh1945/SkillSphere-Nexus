package com.skillsphere.nexus.learning_service.repository;

import com.skillsphere.nexus.learning_service.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByEmpId(UUID empId);

    List<Enrollment> findByCourseCourseId(UUID courseId);

    boolean existsByEmpIdAndCourseCourseId(
            UUID empId,
            UUID courseId);
}