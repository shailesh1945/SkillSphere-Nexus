package com.skillsphere.nexus.learning_service.repository;

import com.skillsphere.nexus.learning_service.model.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseContentRepository
        extends JpaRepository<CourseContent, UUID> {
    List<CourseContent> findByCourseCourseIdOrderBySequenceOrder(UUID courseId);
}
