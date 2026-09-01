package com.skillsphere.nexus.learning_service.service.impl;

import com.skillsphere.nexus.learning_service.model.Course;
import com.skillsphere.nexus.learning_service.model.CourseContent;
import com.skillsphere.nexus.learning_service.repository.CourseContentRepository;
import com.skillsphere.nexus.learning_service.repository.CourseRepository;
import com.skillsphere.nexus.learning_service.service.CourseContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseContentServiceImpl implements CourseContentService {

    private final CourseContentRepository courseContentRepository;
    private final CourseRepository courseRepository;

    @Override
    public CourseContent addContent(UUID courseId, CourseContent content) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        content.setCourse(course);

        return courseContentRepository.save(content);
    }

    @Override
    public List<CourseContent> getCourseContent(UUID courseId) {

        if (!courseRepository.existsById(courseId)) {
            throw new RuntimeException(
                    "Course not found");
        }

        return courseContentRepository.findByCourseCourseIdOrderBySequenceOrder(courseId);
    }
}
