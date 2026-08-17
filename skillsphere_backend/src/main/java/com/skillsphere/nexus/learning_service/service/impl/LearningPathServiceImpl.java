package com.skillsphere.nexus.learning_service.service.impl;

import com.skillsphere.nexus.learning_service.model.Course;
import com.skillsphere.nexus.learning_service.model.LearningPath;
import com.skillsphere.nexus.learning_service.model.LearningPathCourse;
import com.skillsphere.nexus.learning_service.repository.CourseRepository;
import com.skillsphere.nexus.learning_service.repository.LearningPathCourseRepository;
import com.skillsphere.nexus.learning_service.repository.LearningPathRepository;
import com.skillsphere.nexus.learning_service.service.LearningPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearningPathServiceImpl
        implements LearningPathService {

    private final LearningPathRepository pathRepository;
    private final CourseRepository courseRepository;
    private final LearningPathCourseRepository
            pathCourseRepository;

    @Override
    public LearningPath createPath(
            LearningPath path) {
        path.setProgress(0);
        path.setActive(true);

        return pathRepository.save(path);
    }

    @Override
    public LearningPath addCourseToPath(
            UUID pathId, UUID courseId, Integer sequence) {

        LearningPath path =
                pathRepository.findById(pathId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Learning path not found"));

        Course course =
                courseRepository.findById(courseId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Course not found"));

        LearningPathCourse pathCourse =
                LearningPathCourse.builder()
                        .learningPath(path)
                        .course(course)
                        .sequenceOrder(sequence)
                        .build();


                pathCourseRepository.save(pathCourse);

        return path;
    }

    @Override
    public List<LearningPathCourse> getPathCourses(UUID pathId) {

        if (!pathRepository.existsById(pathId)) {
            throw new RuntimeException(
                    "Learning path not found");
        }

        return pathCourseRepository
                .findByLearningPathPathIdOrderBySequenceOrder(pathId);
    }
    }


