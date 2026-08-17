package com.skillsphere.nexus.learning_service.repository;

import com.skillsphere.nexus.learning_service.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    boolean existsByTitleIgnoreCase(String title);

    List<Course> findByActive(Boolean active);

    List<Course> findByInstructorContainingIgnoreCase(String instructor);

    List<Course> findByTitleContainingIgnoreCase(String keyword);
}
